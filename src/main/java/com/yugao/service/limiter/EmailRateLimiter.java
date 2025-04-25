package com.yugao.service.limiter;

import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.service.base.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailRateLimiter {

    @Autowired
    private RedisService redisService;

    public void check(String email) {
        if (redisService.verifyEmailActivationInterval(email)) {
            throw new BusinessException(ResultCode.TOO_SHORT_INTERVAL);
        }
        redisService.deleteEmailActivationInterval(email);
        redisService.setEmailActivationIntervalByMinutes(email);
    }
}
