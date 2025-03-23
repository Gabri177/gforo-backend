package com.yugao.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    public void set(String key, String value);

    public void set(String key, String value, long timeout, TimeUnit unit);

    String get(String key);

    public boolean hasKey(String key);

    public void delete(String key);

    public Long increment(String key, long delta);

    void setTemporarilyByMinutes(String key, String value, long timeoutByMinutes);
}

