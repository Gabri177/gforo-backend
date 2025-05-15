package com.yugao.enums;

import com.yugao.exception.BusinessException;
import lombok.Getter;

@Getter
public enum LikeTypeEnum {
    POST(0, "帖子"),
    COMMENT(1, "评论"),
    USER(2, "用户");

    private final Integer value;
    private final String description;

    LikeTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static LikeTypeEnum getLikeTypeEnum(Integer value) {
        for (LikeTypeEnum type : LikeTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new BusinessException(ResultCodeEnum.INVALID_LIKE_TYPE);
    }
}
