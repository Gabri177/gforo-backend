package com.yugao.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yugao.exception.BusinessException;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TitleConditionTypeEnum implements IEnum<String> {
    EXP("exp", "经验值"),
    POST_COUNT("post_count", "发帖数"),
    COMMENT_COUNT("comment_count", "评论数"),
    SYSTEM("system", "系统"),
    ACTIVITY("activity", "活动"),;

    private final String value;
    private final String desc;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TitleConditionTypeEnum fromValue(String value) {
        for (TitleConditionTypeEnum type : TitleConditionTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new BusinessException(ResultCodeEnum.UNKNOWN_TITLE_CONDITION);
    }
}
