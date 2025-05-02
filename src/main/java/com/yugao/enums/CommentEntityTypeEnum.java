package com.yugao.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;

public enum CommentEntityTypeEnum implements IEnum<Integer> {
    POST(0, "帖子"),
    POST_FLOOR(1, "帖子楼层"),
    POST_COMMENT_FLOOR(2, "帖子评论楼层");

    private final Integer value;
    private final String description;

    CommentEntityTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    @Override
    public Integer getValue() {
        return value;
    }

    @JsonCreator
    public static CommentEntityTypeEnum fromValue(Integer value) {
        for (CommentEntityTypeEnum type : CommentEntityTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new BusinessException(ResultCode.INVALID_STATUS_VALUE);
    }
}
