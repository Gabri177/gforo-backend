package com.yugao.service.impl;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Value("${resetPassword.sixDigVerifyCodeExpireTimeMinutes}")
    private long resetPasswordSixDigVerifyCodeExpireTimeMinutes;

    @Value("${resetPassword.verifiedSixDigVerifyCodeExpireTimeMinutes}")
    private long verifiedSixDigVerifyCodeExpireTimeMinutes;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    private boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }


    @Override
    public void setTemporarilyByMinutes(String key, String value, long timeoutByMinutes) {
        redisTemplate.opsForValue().set(key, value, timeoutByMinutes, TimeUnit.MINUTES);
    }

//    public void setEmailActivationIntervalBy(){
//        setTemporarilyByMinutes(
//                RedisKeyConstants.usernameForgetPasswordSixDigitCode(userForgetPasswordDTO.getUsername()),
//                sixDigVerifyCode,
//                resetPasswordSixDigVerifyCodeExpireTimeMinutes);
//    }
}
