package com.yugao.service.handler;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.auth.DeviceSession;
import com.yugao.domain.user.User;
import com.yugao.dto.auth.RefreshTokenDTO;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.service.base.RedisService;
import com.yugao.util.security.JwtUtil;
import com.yugao.util.serialize.SerializeUtil;
import com.yugao.vo.auth.NewAccessTokenVO;
import com.yugao.vo.auth.TokenInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TokenHandler {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final OnlineUserHandler onlineUserHandler;

    @Value("${jwt.refreshTokenExpiredMillis}")
    private long refreshTokenExpireTimeMillis;

    @Value("${jwt.accessTokenExpiredMillis}")
    private long accessTokenExpireTimeMillis;

    public NewAccessTokenVO refreshAccessToken(RefreshTokenDTO dto){

        String refreshToken = dto.getRefreshToken();
        String deviceId = dto.getSymbol();
        // 这里是接收到的RefreshToken 用来更新AccessToken
        String userId = jwtUtil.getUserIdWithToken(refreshToken);

        if (userId == null) {
            throw new BusinessException(ResultCodeEnum.REFRESHMENT_UNAUTHORIZED);
        }
        String key = RedisKeyConstants.buildUserSessionKey(Long.parseLong(userId));
        Set<String> sessions = redisService.zRange(key, 0, -1);

        for (String json : sessions) {
            DeviceSession session = SerializeUtil.fromJson(json, DeviceSession.class);
            if (session.getDeviceId().equals(deviceId) && session.getRefreshToken().equals(refreshToken)) {

                if (session.getRefreshExpireTimestamp() < System.currentTimeMillis()) {
                    redisService.zRemove(key, json);
                    throw new BusinessException(ResultCodeEnum.REFRESHMENT_EXPIRED);
                }

                String newAccessToken = jwtUtil.generateAccessToken(userId);
                session.setAccessToken(newAccessToken);
                session.setAccessExpireTimestamp(System.currentTimeMillis() + accessTokenExpireTimeMillis);
                redisService.zAdd(key, SerializeUtil.toJson(session), session.getLoginTimestamp());

                NewAccessTokenVO newAccessTokenVO = new NewAccessTokenVO();
                newAccessTokenVO.setNewAccessToken(newAccessToken);
                return newAccessTokenVO;
            }
        }
        throw new BusinessException(ResultCodeEnum.REFRESHMENT_UNAUTHORIZED);
    }

    public TokenInfoVO generateAndStoreToken(User loginUser, String deviceId) {

        String userId = loginUser.getId().toString();
        // 生成token
        String accessToken = jwtUtil.generateAccessToken(userId);
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        long now = System.currentTimeMillis();
        long accessExpireAt = now + accessTokenExpireTimeMillis;
        long refreshExpireAt = now + refreshTokenExpireTimeMillis;

        DeviceSession deviceSession = new DeviceSession();
        deviceSession.setDeviceId(deviceId);
        deviceSession.setAccessToken(accessToken);
        deviceSession.setRefreshToken(refreshToken);
        deviceSession.setLoginTimestamp(now);
        deviceSession.setAccessExpireTimestamp(accessExpireAt);
        deviceSession.setRefreshExpireTimestamp(refreshExpireAt);

        String key = RedisKeyConstants.buildUserSessionKey(loginUser.getId());
        redisService.zAdd(key, SerializeUtil.toJson(deviceSession), now);

        long sessionCount = redisService.zCard(key);
        if (sessionCount > 3)
            redisService.zRemRangeByRank(key, 0, sessionCount - 3);

        // 返回token
        TokenInfoVO tokenInfoVO = new TokenInfoVO();
        tokenInfoVO.setAccess_token(accessToken);
        tokenInfoVO.setRefresh_token(refreshToken);

        return tokenInfoVO;
    }

    // 验证用户访问令牌
    public boolean verifyUserAccessToken(String accessToken, String deviceId) {

        Long userId = Long.parseLong(jwtUtil.getUserIdWithToken(accessToken));
        String key = RedisKeyConstants.buildUserSessionKey(userId);
        Set<String> sessions = redisService.zRange(key, 0, -1);

        for (String json : sessions) {
            DeviceSession session = SerializeUtil.fromJson(json, DeviceSession.class);
            if (session.getAccessToken().equals(accessToken) &&
                    session.getDeviceId().equals(deviceId) &&
            session.getAccessExpireTimestamp() > System.currentTimeMillis()) {

                onlineUserHandler.recordOnline(userId);
                System.out.println("在线用户: " + userId);
                return true;
            }
        }
        return false;
    }

    public void forceLogout(Long userId, String deviceId){

        String key = RedisKeyConstants.buildUserSessionKey(userId);
        Set<String> sessions = redisService.zRange(key, 0, -1);
        for (String json : sessions) {
            DeviceSession session = SerializeUtil.fromJson(json, DeviceSession.class);
            if (session.getDeviceId().equals(deviceId)) {
                redisService.zRemove(key, json);
                break;
            }
        }
    }
}
