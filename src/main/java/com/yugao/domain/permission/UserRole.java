package com.yugao.domain.permission;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yugao.enums.RoleEnum;
import lombok.Data;

@Data
@TableName("user_role")
public class UserRole {

//    user_id	bigint	NO	PRI
//    role_id	bigint	NO	PRI

    private Long userId;

    private Long roleId;

    public UserRole(Long id, RoleEnum roleEnum) {
        this.userId = id;
        this.roleId = roleEnum.getCode();
    }

    public UserRole(Long id, Long roleId){
        this.userId = id;
        this.roleId = roleId;
    }
}
