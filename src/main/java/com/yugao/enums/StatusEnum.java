package com.yugao.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yugao.exception.BusinessException;

public enum StatusEnum implements IEnum<Integer> {
    NORMAL(0, "正常"),
    DELETED(1, "删除");

    private final Integer value;
    private final String description;

    StatusEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    @Override
    public Integer getValue() {
        return value;
    }

    @JsonCreator
    public static StatusEnum fromValue(Integer value) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new BusinessException(ResultCodeEnum.INVALID_STATUS_VALUE);
    }
}
