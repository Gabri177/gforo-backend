package com.yugao.service.business.session.impl;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.auth.DeviceSession;
import com.yugao.netty.registry.ChannelRegistry;
import com.yugao.service.base.RedisService;
import com.yugao.service.business.session.SessionService;
import com.yugao.service.handler.OnlineUserHandler;
import com.yugao.util.serialize.SerializeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final RedisService redisService;
    private final ChannelRegistry channelRegistry;
    private final OnlineUserHandler onlineUserHandler;

    @Override
    public Set<DeviceSession> getSessions(Long userId) {
        String key = RedisKeyConstants.buildUserSessionKey(userId);
        return redisService.zRange(key, 0, -1)
                .stream()
                .map(json -> SerializeUtil.fromJson(json, DeviceSession.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<DeviceSession> findSession(Long userId, String deviceId) {
        return getSessions(userId).stream()
                .filter(session -> session.getDeviceId().equals(deviceId))
                .findFirst();
    }

    @Override
    public void updateSession(Long userId, DeviceSession session) {
        String key = RedisKeyConstants.buildUserSessionKey(userId);
        redisService.zAdd(key, SerializeUtil.toJson(session), session.getLoginTimestamp());
    }

    @Override
    public void removeSession(Long userId, DeviceSession session) {
        String key = RedisKeyConstants.buildUserSessionKey(userId);
        redisService.zRemove(key, SerializeUtil.toJson(session));
        onlineUserHandler.recordOffline(userId);
        channelRegistry.remove(userId.toString(), session.getDeviceId());
    }

    @Override
    public void keepSessionMax(Long userId, int max) {
        String key = RedisKeyConstants.buildUserSessionKey(userId);
        long sessionCount = redisService.zCard(key);
        if (sessionCount > max) {
            redisService.zRemRangeByRank(key, 0, sessionCount - max - 1);
        }
    }

    @Override
    public boolean isDeviceSessionExist(Long userId, String deviceId) {
        return findSession(userId, deviceId).isPresent();
    }

    @Override
    public Long countSession(Long userId) {

        String key = RedisKeyConstants.buildUserSessionKey(userId);
        return redisService.zCard(key);
    }
}
