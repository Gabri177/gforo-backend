package com.yugao.service.base.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.service.base.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void set(String key, String value) {

        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    @Override
    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public boolean hasKey(String key) {
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

    @Override
    public void setObjectTemmporarilyByMinutes(String key, Object value, long timeoutByMinutes) {
        try {
            String val = objectMapper.writeValueAsString(value);
            setTemporarilyByMinutes(key, val, timeoutByMinutes);
        } catch (Exception e) {
            throw new BusinessException(ResultCodeEnum.REDIS_SAVING_ERROR);
        }
    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        String json = get(key);
        if (json == null)
            throw new BusinessException(ResultCodeEnum.VERIFY_EXPIRED);
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new BusinessException(ResultCodeEnum.JSON_PARSE_ERROR);
        }
    }

    @Override
    public void increment(String key, int i) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            redisTemplate.opsForValue().set(key, String.valueOf(i));
        } else {
            int newValue = Integer.parseInt(value) + i;
            redisTemplate.opsForValue().set(key, String.valueOf(newValue));
        }
    }

    @Override
    public void decrement(String key, int i) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            redisTemplate.opsForValue().set(key, String.valueOf(-i));
        } else {
            int newValue = Integer.parseInt(value) - i;
            redisTemplate.opsForValue().set(key, String.valueOf(newValue));
        }
    }

    @Override
    public void zAdd(String key, String value, double score) {

        redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public void expire(String key, long timeout, TimeUnit unit) {

        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public void zRemove(String key, String value) {

        redisTemplate.opsForZSet().remove(key, value);
    }

    @Override
    public void zScore(String key, String value) {

        redisTemplate.opsForZSet().score(key, value);
    }

    @Override
    public Long zCard(String key) {

        return redisTemplate.opsForZSet().size(key);
    }

    @Override
    public void zRemRangeByRank(String key, long start, long end) {

        redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    @Override
    public Set<String> zRange(String key, long start, long end) {

        return redisTemplate.opsForZSet().range(key, start, end);
    }

    @Override
    public void sAdd(String key, String value) {

        redisTemplate.opsForSet().add(key, value);
    }

    @Override
    public Long sCard(String key) {

        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Set<String> sMembers(String key) {

        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public void sRemove(String key, String value) {

        redisTemplate.opsForSet().remove(key, value);
    }

}
