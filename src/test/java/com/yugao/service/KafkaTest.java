package com.yugao.service;

import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@SpringBootTest
public class KafkaTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Test
    public void testKafka(){
        kafkaProducer.send("test", "Hello, Kafka!");
        kafkaProducer.send("test", "Hello, Kafka!");

        try {
            Thread.sleep(10000); // Wait for a while to receive messages
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

@Component
class KafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}

@Component
class KafkaConsumer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @KafkaListener(topics = "test")
    public void handleMessage(ConsumerRecord record) {
        System.out.println("Received message: " + record.value());
    }
}