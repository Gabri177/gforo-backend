package com.yugao.util.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;

public class SerializeUtil {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new BusinessException(ResultCodeEnum.SERIALIZATION_ERROR);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
//            System.out.println("json = " + json);
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new BusinessException(ResultCodeEnum.DESERIALIZATION_ERROR);
        }
    }
}
