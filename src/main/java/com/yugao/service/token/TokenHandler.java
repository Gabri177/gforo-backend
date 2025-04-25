package com.yugao.service.token;

import com.yugao.domain.User;
import com.yugao.domain.UserToken;
import com.yugao.dto.UserRegisterDTO;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultResponse;
import com.yugao.service.base.RedisService;
import com.yugao.service.data.UserTokenService;
import com.yugao.util.security.JwtUtil;
import com.yugao.util.security.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserTokenService userTokenService;

    @Value("${jwt.refreshTokenExpiredMillis}")
    private long refreshTokenExpireTimeMillis;

    public void invalidateExistingToken(Long userId) {

        UserToken onlineUserToken = userTokenService.findByUserId(userId);
        if (onlineUserToken != null) {
            System.out.println("User already logged in. Deleting previous login information");
            Boolean isDeleted = userTokenService.deleteUserTokenByUserId(onlineUserToken.getUserId());
            if (!isDeleted) {
                throw new BusinessException(ResultCode.SQL_EXCEPTION);
            }
        }
    }

    public void invalidateAccessToken(String accessToken) {

        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
        accessToken = accessToken.replace("Bearer ", "");
        String userId = jwtUtil.getUserIdWithToken(accessToken);
        if (userId == null) {
            throw new BusinessException(ResultCode.ACCESSTOKEN_UNAUTHORIZED);
        }
        System.out.println("logout UserId: " + userId);
        // 删除数据库中的 user_token 登录数据
        Boolean isLogout = userTokenService.deleteUserTokenByUserId(Long.parseLong(userId));
        System.out.println("isLogout: " + isLogout);
        if (!isLogout) {
            throw new BusinessException(ResultCode.LOGOUT_WITHOUT_LOGIN);
        }
    }

    public Map<String, String> refreshAccessToken(String refreshToken){

//        System.out.println("refresh token: " + refreshToken);
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")){
            throw new BusinessException(ResultCode.REFRESHTOKEN_UNAUTHORIZED);
        }

        refreshToken = refreshToken.replace("Bearer ", "");

        // 这里是接收到的RefreshToken 用来更新AccessToken
        String userId = jwtUtil.getUserIdWithToken(refreshToken);
        if (userId == null) {
            throw new BusinessException(ResultCode.REFRESHTOKEN_UNAUTHORIZED);
        }
        UserToken userToken = userTokenService.findByUserId(Long.parseLong(userId));
        if (userToken == null || !userToken.getRefreshToken().equals(refreshToken)) {
//            System.out.println("Invalid refresh token");
            throw new BusinessException(ResultCode.REFRESHTOKEN_UNAUTHORIZED);
        }

        // 检查数据库中的 refresh token 是否过期
        if (userToken.getExpiresAt().getTime() < System.currentTimeMillis()) {
            // refreshToken过期 删除数据库中的 user_token 登录数据, 同一个用户不能重复登录
            userTokenService.deleteUserTokenByUserId(Long.parseLong(userId));
            redisService.deleteUserAccessToken(Long.parseLong(userId));
            throw new BusinessException(ResultCode.REFRESHTOKEN_EXPIRED);
        }

        // 没有过期 生成新的AccessToken
        String newAccessToken = jwtUtil.generateAccessToken(userId);
        redisService.setUserAccessToken(Long.parseLong(userId), newAccessToken);
        userTokenService.updateAccessToken(Long.parseLong(userId), newAccessToken);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("newAccessToken", newAccessToken);
        return resultMap;
    }


    public Map<String, String> generateAndStoreToken(Long userId, User loginUser) {
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
        redisService.setUserAccessToken(loginUser.getId(), accessToken);
        // 存储到Sql中
        try {
            userTokenService.saveUserToken(userToken);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.SQL_EXCEPTION);
        }

        // 返回token
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("access_token", accessToken);
        resultMap.put("refresh_token", refreshToken);
        return resultMap;
    }
}
