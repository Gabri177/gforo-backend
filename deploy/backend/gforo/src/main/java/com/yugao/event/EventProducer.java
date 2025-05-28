package com.yugao.event;

import com.yugao.domain.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, Event<?>> kafkaTemplate;

    public void send(String topic, Event<?> event) {
        kafkaTemplate.send(topic, event);
    }

    public void send(String topic, String key, Event<?> event) {
        kafkaTemplate.send(topic, key, event);
    }

}
