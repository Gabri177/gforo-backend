package com.yugao.enums;

import com.yugao.exception.BusinessException;
import lombok.Getter;

@Getter
public enum BooleanEnum {
    TRUE("true", "是"),
    FALSE("false", "否");

    private final String value;
    private final String description;

    BooleanEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static BooleanEnum getBooleanEnum(String value) {
        for (BooleanEnum type : BooleanEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new BusinessException(ResultCodeEnum.INVALID_BOOLEAN_ENUM);
    }
}
