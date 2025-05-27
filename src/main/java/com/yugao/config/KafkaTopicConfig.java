package com.yugao.config;

import com.yugao.constants.KafkaTopicConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    // TODO: 这里的分区数和副本数需要根据实际情况进行调整

    @Bean
    public NewTopic notificationLikeTopic() {
        return TopicBuilder.name(KafkaTopicConstants.NOTIFICATION_LIKE)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationCommentTopic() {
        return TopicBuilder.name(KafkaTopicConstants.NOTIFICATION_COMMENT)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationFollowTopic() {
        return TopicBuilder.name(KafkaTopicConstants.NOTIFICATION_FOLLOW)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationMessageTopic() {
        return TopicBuilder.name(KafkaTopicConstants.MESSAGE)
                .partitions(5) // 可根据实际并发量设置
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationSystemTopic() {
        return TopicBuilder.name(KafkaTopicConstants.NOTIFICATION_SYSTEM)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sendVerificationEmailTopic() {
        return TopicBuilder.name(KafkaTopicConstants.SEND_VERIFICATION_EMAIL)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
