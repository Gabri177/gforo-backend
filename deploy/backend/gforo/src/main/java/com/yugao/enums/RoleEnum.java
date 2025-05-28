package com.yugao.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yugao.exception.BusinessException;
import lombok.Getter;

@Getter
public enum RoleEnum implements IEnum<Long> {
    ROLE_USER(1L, "普通用户"),
    ROLE_BOARD(2L, "版主"),
    ROLE_ADMIN(3L, "管理员");

    private final Long code;
    private final String msg;

    RoleEnum(Long code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @JsonValue
    @Override
    public Long getValue() {
        return code;
    }

    @JsonCreator
    public static RoleEnum fromValue(Long code) {

        for (RoleEnum role : RoleEnum.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
         // 这里可以抛出一个自定义的异常，表示无效的状态值
        throw new BusinessException(ResultCodeEnum.INVALID_ROLE);
    }
}
