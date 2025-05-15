package com.yugao.service.base;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisService {

    void set(String key, String value);

    String get(String key);

    void set(String key, String value, long timeout, TimeUnit unit);

    boolean hasKey(String key);

    void delete(String key);

    void setTemporarilyByMinutes(String key, String value, long timeoutByMinutes);

    void setObjectTemmporarilyByMinutes(String key, Object value, long timeoutByMinutes);

    <T> T getObject(String key, Class<T> clazz);

    void increment(String key, int i);

    void decrement(String key, int i);

    void zAdd(String key, String value, double score);

    void expire(String key, long timeout, TimeUnit unit);

    void zRemove(String key, String value);

    void zScore(String key, String value);

    Long zCard(String key);

    void zRemRangeByRank(String key, long start, long end);

    Set<String> zRange(String key, long start, long end);

}

