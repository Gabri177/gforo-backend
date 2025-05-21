package com.yugao.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yugao.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntityTypeEnum implements IEnum<Integer> {
    NULL(0, "无"),
    POST(1, "帖子"),
    COMMENT(2, "评论"),
    TITLE(3, "称号"),;

    private final Integer value;
    private final String description;


    @JsonValue
    @Override
    public Integer getValue() {
        return value;
    }

    @JsonCreator
    public static EntityTypeEnum fromValue(Integer value) {
        for (EntityTypeEnum type : EntityTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new BusinessException(ResultCodeEnum.ENTITY_TYPE_NOT_FOUND);
    }

}
