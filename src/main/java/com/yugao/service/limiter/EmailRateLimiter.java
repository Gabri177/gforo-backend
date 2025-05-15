package com.yugao.service.limiter;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.service.base.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailRateLimiter {

    private final RedisService redisService;

    @Value("${email.active-account.request-interval-time-minutes}")
    private Long emailRequestIntervalTimeMinutes;

    // 设置邮箱激活请求的时间标志
    private void setEmailActivationIntervalByMinutes(String email) {
        redisService.setTemporarilyByMinutes(
                RedisKeyConstants.buildEmailActivationIntervalKey(email),
                "true",
                emailRequestIntervalTimeMinutes);
    }
    // 判断请求激活的时间标志是否过期
    private boolean verifyEmailActivationInterval(String email) {
        return redisService.hasKey(RedisKeyConstants.buildEmailActivationIntervalKey(email)) &&
                "true".equals(redisService.get(RedisKeyConstants.buildEmailActivationIntervalKey(email)));
    }
    // 删除邮箱激活请求的时间标志
    private void deleteEmailActivationInterval(String email) {
        redisService.delete(RedisKeyConstants.buildEmailActivationIntervalKey(email));
    }

    public void check(String email) {
        if (verifyEmailActivationInterval(email)) {
            throw new BusinessException(ResultCodeEnum.TOO_SHORT_INTERVAL);
        }
        deleteEmailActivationInterval(email);
        setEmailActivationIntervalByMinutes(email);
    }
}
