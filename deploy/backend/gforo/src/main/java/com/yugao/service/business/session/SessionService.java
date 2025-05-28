package com.yugao.service.business.session;

import com.yugao.domain.auth.DeviceSession;

import java.util.Optional;
import java.util.Set;

public interface SessionService {

    Set<DeviceSession> getSessions(Long userId);
    Optional<DeviceSession> findSession(Long userId, String deviceId);
    void updateSession(Long userId, DeviceSession session);
    void removeSession(Long userId, DeviceSession session);
    void keepSessionMax(Long userId, int max);
    boolean isDeviceSessionExist(Long userId, String deviceId);
    Long countSession(Long userId);
}
