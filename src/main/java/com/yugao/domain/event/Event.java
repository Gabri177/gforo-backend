package com.yugao.domain.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event<T> {

    private String eventId;
    private String eventType;
    private String aggregateType; // 聚合根类型
    private String aggregateId; // 聚合根ID
    private long timestamp;
    private T payload;
    private Map<String, Object> metadata;

    public static <T> Event<T> create(String type, String aggType, String aggId, T data) {
        return Event.<T>builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(type)
                .aggregateType(aggType)
                .aggregateId(aggId)
                .timestamp(System.currentTimeMillis())
                .payload(data)
                .build();
    }

    public T getPayloadAs(Class<T> clazz, ObjectMapper mapper) {
        return mapper.convertValue(payload, clazz);
    }

    @SuppressWarnings("unchecked")
    public <M> M getMetadataValue(String key, Class<M> clazz, ObjectMapper mapper) {
        Object value = metadata.get(key);
        return mapper.convertValue(value, clazz);
    }

}
