package com.yugao.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yugao.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationTypeEnum implements IEnum<Integer> {

    COMMENT_COMMENT(0, "评论评论"),
    COMMENT_POST(1, "帖子评论"),
    LIKE(2, "点赞"),
    FOLLOW(3, "关注"),
    MESSAGE(4, "私信"),
    SYSTEM(5, "系统通知"),
    ADMIN(6, "管理员通知");


    private final Integer value;
    private final String description;

    @JsonValue
    @Override
    public Integer getValue() {
        return this.value;
    }

    @JsonCreator
    public static NotificationTypeEnum fromValue(Integer value) {
        for (NotificationTypeEnum type : NotificationTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new BusinessException(ResultCodeEnum.INVALID_NOTIFICATION_TYPE);
    }
}
