package com.yugao.service.handler;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.auth.DeviceSession;
import com.yugao.domain.user.User;
import com.yugao.dto.auth.RefreshTokenDTO;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.netty.registry.ChannelRegistry;
import com.yugao.service.business.session.SessionService;
import com.yugao.util.security.JwtUtil;
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
    private final OnlineUserHandler onlineUserHandler;
    private final SessionService sessionService;

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
        Long userIdLong = Long.parseLong(userId);
        String key = RedisKeyConstants.buildUserSessionKey(userIdLong);
        Set<DeviceSession> sessions = sessionService.getSessions(userIdLong);

        for (DeviceSession session : sessions) {
            if (session.getDeviceId().equals(deviceId) && session.getRefreshToken().equals(refreshToken)) {

                if (session.getRefreshExpireTimestamp() < System.currentTimeMillis()) {
                    sessionService.removeSession(userIdLong, session);
                    throw new BusinessException(ResultCodeEnum.REFRESHMENT_EXPIRED);
                }

                String newAccessToken = jwtUtil.generateAccessToken(userId);
                session.setAccessToken(newAccessToken);
                session.setAccessExpireTimestamp(System.currentTimeMillis() + accessTokenExpireTimeMillis);
                sessionService.updateSession(userIdLong, session);

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

        sessionService.updateSession(loginUser.getId(), deviceSession);
        sessionService.keepSessionMax(loginUser.getId(), 3);

        // 返回token
        TokenInfoVO tokenInfoVO = new TokenInfoVO();
        tokenInfoVO.setAccess_token(accessToken);
        tokenInfoVO.setRefresh_token(refreshToken);

        return tokenInfoVO;
    }

    // 验证用户访问令牌
    public boolean verifyUserAccessToken(String accessToken, String deviceId) {

        String res = jwtUtil.getUserIdWithToken(accessToken);
        if (res == null)
            return false;
        Long userId = Long.parseLong(res);
        Set<DeviceSession> sessions = sessionService.getSessions(userId);

        for (DeviceSession session : sessions) {
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

        Set<DeviceSession> sessions = sessionService.getSessions(userId);
        for (DeviceSession session : sessions) {
            if (session.getDeviceId().equals(deviceId)) {
                sessionService.removeSession(userId, session);
                break;
            }
        }
    }
}
