package com.yugao.service.base;

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
}

