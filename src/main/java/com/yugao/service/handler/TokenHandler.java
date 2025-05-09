package com.yugao.service.handler;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.user.User;
import com.yugao.domain.auth.UserToken;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.service.base.RedisService;
import com.yugao.service.data.UserTokenService;
import com.yugao.util.security.JwtUtil;
import com.yugao.vo.auth.NewAccessTokenVO;
import com.yugao.vo.auth.TokenInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    @Value("${jwt.accessTokenExpiredMillis}")
    private long accessTokenExpireTimeMillis;

    public void invalidateExistingToken(Long userId) {

        UserToken onlineUserToken = userTokenService.findByUserId(userId);
        if (onlineUserToken != null) {
            System.out.println("User already logged in. Deleting previous login information");
            Boolean isDeleted = userTokenService.deleteUserTokenByUserId(onlineUserToken.getUserId());
            if (!isDeleted) {
                throw new BusinessException(ResultCodeEnum.SQL_EXCEPTION);
            }
        }
    }

    /**
     * 参数名字不对 !!!!!!!!!!!!!!!!!!!!!!!
     * @param refreshToken
     * @return
     */
    public NewAccessTokenVO refreshAccessToken(String refreshToken){

//        System.out.println("refresh token: " + refreshToken);

        // 这里是接收到的RefreshToken 用来更新AccessToken
        String userId = jwtUtil.getUserIdWithToken(refreshToken);
        if (userId == null) {
            throw new BusinessException(ResultCodeEnum.REFRESHMENT_UNAUTHORIZED);
        }
        UserToken userToken = userTokenService.findByUserId(Long.parseLong(userId));
        if (userToken == null || !userToken.getRefreshToken().equals(refreshToken)) {
//            System.out.println("Invalid refresh token");
            throw new BusinessException(ResultCodeEnum.REFRESHMENT_UNAUTHORIZED);
        }

        // 检查数据库中的 refresh token 是否过期
        if (userToken.getExpiresAt().getTime() < System.currentTimeMillis()) {

            userTokenService.deleteUserTokenByUserId(Long.parseLong(userId));
            deleteUserAccessToken(Long.parseLong(userId));
            throw new BusinessException(ResultCodeEnum.REFRESHMENT_EXPIRED);
        }

        // 没有过期 生成新的AccessToken
        String newAccessToken = jwtUtil.generateAccessToken(userId);
        setUserAccessToken(Long.parseLong(userId), newAccessToken);
        userTokenService.updateAccessToken(Long.parseLong(userId), newAccessToken);

//        Map<String, String> resultMap = new HashMap<>();
//        resultMap.put("newAccessToken", newAccessToken);
//        return resultMap;
        NewAccessTokenVO newAccessTokenVO = new NewAccessTokenVO();
        newAccessTokenVO.setNewAccessToken(newAccessToken);
        return newAccessTokenVO;
    }

    public TokenInfoVO generateAndStoreToken(User loginUser) {
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
        setUserAccessToken(loginUser.getId(), accessToken);
        // 存储到Sql中
        try {
            userTokenService.saveUserToken(userToken);
        } catch (Exception e) {
            throw new BusinessException(ResultCodeEnum.SQL_EXCEPTION);
        }

        // 返回token
        TokenInfoVO tokenInfoVO = new TokenInfoVO();
        tokenInfoVO.setAccess_token(accessToken);
        tokenInfoVO.setRefresh_token(refreshToken);
//        Map<String, String> resultMap = new HashMap<>();
//        resultMap.put("access_token", accessToken);
//        resultMap.put("refresh_token", refreshToken);
//        return resultMap;
        return tokenInfoVO;
    }

    private void setUserAccessToken(Long userId, String accessToken) {
        redisService.set(RedisKeyConstants.buildUserIdAccessTokenKey(userId), accessToken,
                accessTokenExpireTimeMillis, TimeUnit.MILLISECONDS);
    }
    // 删除用户访问令牌
    private void deleteUserAccessToken(Long userId) {

        redisService.delete(RedisKeyConstants.buildUserIdAccessTokenKey(userId));
    }
    // 验证用户访问令牌
    public boolean verifyUserAccessToken(String accessToken){
        Long userId = Long.parseLong(jwtUtil.getUserIdWithToken(accessToken));
        String redisAccessToken = redisService.get(RedisKeyConstants.buildUserIdAccessTokenKey(userId));
        return accessToken != null && accessToken.equals(redisAccessToken);
    }
}
