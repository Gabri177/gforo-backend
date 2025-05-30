package com.yugao.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yugao.exception.BusinessException;

public enum StatusEnum implements IEnum<Integer> {
    NORMAL(1, "正常"),
    DELETED(0, "删除"),
    DISABLED(2, "禁用"),
    PENDING(3, "待处理"),
    APPROVED(4, "已处理"),
    REJECTED(5, "驳回"),
    NULL(6, "无效状态");

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
