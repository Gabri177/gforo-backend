package com.yugao.service.handler;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.user.User;
import com.yugao.service.base.RedisService;
import com.yugao.service.data.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final RedisService redisService;
    private final UserService userService;

    public User getUser(Long userId){
        String key = RedisKeyConstants.buildUserCacheKey(userId);
        User obj;
        if (!redisService.hasKey(key)){
            obj = userService.getUserById(userId);
            if (obj == null)
                return null;
            // 这里通过配置项进行配置
            // 无论是从数据库得到的 还是从redis得到的 都是需要放入redis的 要么放入 要么刷新过期时间
            redisService.setObjectTemmporarilyByMinutes(key, obj, 3);
            return obj;
        }
        obj = redisService.getObject(key, User.class);
        redisService.setObjectTemmporarilyByMinutes(key, obj, 3);
        return obj;
    }

    public void updateUserCache(Long userId){
        User user = userService.getUserById(userId);
        String key = RedisKeyConstants.buildUserCacheKey(userId);
        redisService.setObjectTemmporarilyByMinutes(key, user, 3);
    }

    public void deleteUserCache(Long userId){
        String key = RedisKeyConstants.buildUserCacheKey(userId);
        redisService.delete(key);
    }
}
