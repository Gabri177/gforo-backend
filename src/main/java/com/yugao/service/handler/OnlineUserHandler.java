package com.yugao.service.handler;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.user.User;
import com.yugao.service.base.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OnlineUserHandler {

    private final RedisService redisService;
    private final UserHandler userHandler;
    private static final String KEY = RedisKeyConstants.ONLINE_USER;
    private static final String DAILY_ACTIVE_KEY = RedisKeyConstants.DAILY_ACTIVE_USER;
    private static final String MONTHLY_ACTIVE_KEY = RedisKeyConstants.MONTHLY_ACTIVE_USER;


    public void recordOnline(Long userId) {
        redisService.sAdd(KEY, userId.toString());
        redisService.expire(KEY, 3, TimeUnit.MINUTES); // 可调

        // 记录日活
        redisService.sAdd(DAILY_ACTIVE_KEY, userId.toString());
        redisService.expire(DAILY_ACTIVE_KEY, 36, TimeUnit.HOURS);
        // 记录月活
        redisService.sAdd(MONTHLY_ACTIVE_KEY, userId.toString());
        redisService.expire(MONTHLY_ACTIVE_KEY, 40, TimeUnit.DAYS);
    }

    public void recordOffline(Long userId) {

        redisService.sRemove(KEY, userId.toString());
    }

    public long getOnlineCount() {
        return redisService.sCard(KEY);
    }

    public Set<User> getOnlineUsers() {
        Set<String> userJson =  redisService.sMembers(KEY);
        return userJson.stream()
                .map(userId -> userHandler.getUser(Long.parseLong(userId)))
                .collect(Collectors.toSet());
    }
}

