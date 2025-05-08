package com.yugao.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role_permission")
public class RolePermission {

//    role_id	bigint	NO	PRI
//    permission_id	bigint	NO	PRI

    private Long roleId;

    private Long permissionId;

    public RolePermission(Long roleId, Long permissionId) {
    }
}
