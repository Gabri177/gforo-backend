package com.yugao.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    void set(String key, String value);

    void set(String key, String value, long timeout, TimeUnit unit);

    String get(String key);

    boolean hasKey(String key);

    void delete(String key);

    Long increment(String key, long delta);

    void setTemporarilyByMinutes(String key, String value, long timeoutByMinutes);
}

