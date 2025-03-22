package com.yugao.controller;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.User;
import com.yugao.domain.UserToken;
import com.yugao.dto.UserDTO;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.UserService;
import com.yugao.service.UserTokenService;
import com.yugao.util.EncryptedUtil;
import com.yugao.util.JwtUtil;
import com.yugao.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.accessTokenExpiredMillis}")
    private long accessTopkenExpireTimeMillis;

    @Value("${jwt.refreshTokenExpiredMillis}")
    private long refreshTokenExpireTimeMillis;

    /**
     * 验证是否通过验证 并登录
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ResultFormat> login(
            @Validated({ValidationGroups.Login.class}) @RequestBody UserDTO userDTO) {

        // 从 Redis 读取用户是否通过了验证码验证
        // String redisValidateStatusKey = "captcha_verified:" + user.getUsername();
        String redisValidateStatusKey = RedisKeyConstants.usernameCaptchaVerified(userDTO.getUsername());
        String redisValidateStatus = redisTemplate.opsForValue().get(redisValidateStatusKey);

        // 检查验证码是否正确 防止用接口恶意登录
        if (redisValidateStatus == null || !redisValidateStatus.equalsIgnoreCase("true")) {
            return ResultResponse.error(ResultCode.LOGIN_WITHOUT_CAPTCHA, "You have not passed the captcha verification");
        }

        User loginUser = userService.getUserByName(userDTO.getUsername());
        if (loginUser == null) {
            return ResultResponse.error("User does not exist");
        }
        String passwd = EncryptedUtil.md5(userDTO.getPassword() + loginUser.getSalt());
        if (!loginUser.getPassword().equals(passwd)) {
            return ResultResponse.error("Error password");
        }

        // 删除redis中存储的验证码
        redisTemplate.delete(redisValidateStatusKey);

        // 检查用户是否已经登录 如果已经登录 则删除之前的登录信息
        // 这样之前的用户无法刷新access token 除非重新登录
        // 同一个时间仅允许一个用户登录
        UserToken onlineUserToken = userTokenService.findByUserId(loginUser.getId());
        if (onlineUserToken != null) {
            System.out.println("User already logged in. Deleting previous login information");
            Boolean isDeleted = userTokenService.deleteUserTokenByUserId(onlineUserToken.getUserId());
            if (!isDeleted) {
                throw new BusinessException("Repeat Login SQL Error: deleting token failed.");
            }
        }

        // 生成token
        String accessToken = jwtUtil.generateAccessToken(loginUser.getId().toString());
        String refreshToken = jwtUtil.generateRefreshToken(loginUser.getId().toString());
        UserToken userToken = new UserToken();
        userToken.setAccessToken(accessToken);
        userToken.setRefreshToken(refreshToken);
        userToken.setUserId(loginUser.getId());

        LocalDateTime expiresAt = LocalDateTime.now()
                .plus(Duration.ofMillis(refreshTokenExpireTimeMillis));
        userToken.setExpiresAt(Date.from(expiresAt
                .atZone(ZoneId.systemDefault())
                .toInstant()));
        // 存储到redis中
        // "access_token:" + loginUser.getId()
        redisTemplate.opsForValue().set(RedisKeyConstants.userIdAccessToken(loginUser.getId()),
                accessToken, accessTopkenExpireTimeMillis, TimeUnit.MILLISECONDS);
        // 存储到Sql中
        try {
            userTokenService.saveUserToken(userToken);
        } catch (Exception e) {
            throw new BusinessException("Error saving token");
        }

        // 返回token
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("access_token", accessToken);
        resultMap.put("refresh_token", refreshToken);
        return ResultResponse.success(resultMap);
    }

    /**
     * 退出登录
     * @return
     */
    @DeleteMapping("/logout")
    public ResponseEntity<ResultFormat> logout(@RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            return ResultResponse.error(HttpStatus.UNAUTHORIZED, ResultCode.TOKEN_INVALID,
                    "Invalid access token, illegal logout");
        }
        accessToken = accessToken.replace("Bearer ", "");
        String userId = jwtUtil.getUserIdWithToken(accessToken);
        if (userId == null) {
            return ResultResponse.error(HttpStatus.UNAUTHORIZED, ResultCode.ACCESSTOKEN_UNAUTHORIZED,
                    "Access token is invalid: unauthorized logout");
        }
        System.out.println("logout UserId: " + userId);
        // 删除数据库中的 user_token 登录数据
        Boolean isLogout = userTokenService.deleteUserTokenByUserId(Long.parseLong(userId));
        System.out.println("isLogout: " + isLogout);
        if (!isLogout) {
            return ResultResponse.error(HttpStatus.UNAUTHORIZED, ResultCode.LOGOUT_WITHOUT_LOGIN,
                    "You are not logged in, please login first");
        }
        return ResultResponse.success("Logout successfully");
    }

    /**
     * 刷新token
     * @param refreshToken
     * @return
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ResultFormat> refresh(@RequestHeader("Authorization") String refreshToken) {

//        System.out.println("refresh token: " + refreshToken);
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")){
            return ResultResponse.error(HttpStatus.UNAUTHORIZED, ResultCode.REFRESHTOKEN_UNAUTHORIZED, "Invalid refresh token");
        }

        refreshToken = refreshToken.replace("Bearer ", "");

        // 这里是接收到的RefreshToken 用来更新AccessToken
        String userId = jwtUtil.getUserIdWithToken(refreshToken);
        if (userId == null) {
            return ResultResponse.error(HttpStatus.UNAUTHORIZED, ResultCode.REFRESHTOKEN_UNAUTHORIZED, "Invalid refresh token");
        }
        UserToken userToken = userTokenService.findByUserId(Long.parseLong(userId));
        if (userToken == null || !userToken.getRefreshToken().equals(refreshToken)) {
//            System.out.println("Invalid refresh token");
            return ResultResponse.error(HttpStatus.UNAUTHORIZED, ResultCode.REFRESHTOKEN_UNAUTHORIZED,"Invalid refresh token");
        }

        // 检查数据库中的 refresh token 是否过期
        if (userToken.getExpiresAt().getTime() < System.currentTimeMillis()) {
            // refreshToken过期 删除数据库中的 user_token 登录数据, 同一个用户不能重复登录
            userTokenService.deleteUserTokenByUserId(Long.parseLong(userId));
            redisTemplate.delete("access_token:" + userId);
//            System.out.println("Refresh token is expired");
            return ResultResponse.error(HttpStatus.FORBIDDEN, ResultCode.REFRESHTOKEN_EXPIRED,"Refresh token is expired");
        }

        // 没有过期 生成新的AccessToken
        String newAccessToken = jwtUtil.generateAccessToken(userId);
        redisTemplate.opsForValue().set("access_token:" + userId, newAccessToken, accessTopkenExpireTimeMillis, TimeUnit.MILLISECONDS);
        userTokenService.updateAccessToken(Long.parseLong(userId), newAccessToken);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("newAccessToken", newAccessToken);
        return ResultResponse.success(resultMap);
    }

}
