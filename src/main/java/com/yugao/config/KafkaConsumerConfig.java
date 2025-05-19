package com.yugao.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

    /**
     * 全局 Kafka 异常处理器
     */
    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {
        // 最多重试3次 间隔1秒钟，然后失败 并丢弃消息
        FixedBackOff backOff = new FixedBackOff(1000L, 3L);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler((record, exception) -> {
            // 也可以把这里的异常转发到死信队列（DLQ）
            // 考虑是不是要用死信队列 或者丢弃信息
            log.error("Kafka 消费异常处理 - Topic: {}, Offset: {}, 错误: {}",
                    record.topic(), record.offset(), exception.getMessage(), exception);
        }, backOff);

        // 记录重试信息
        errorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.warn("第 {} 次尝试处理失败记录: topic={}, offset={}",
                    deliveryAttempt, record.topic(), record.offset());
        });

        return errorHandler;
    }
}
