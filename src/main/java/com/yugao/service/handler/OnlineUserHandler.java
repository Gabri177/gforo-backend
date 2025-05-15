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
    private static final String KEY = RedisKeyConstants.ONLINE_USER;
    private final UserHandler userHandler;


    public void recordOnline(Long userId) {
        redisService.sAdd(KEY, userId.toString());
        redisService.expire(KEY, 3, TimeUnit.MINUTES); // 可调
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

