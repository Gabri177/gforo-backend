package com.yugao.domain.permission;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_permission_override")
public class UserPermissionOverride {

//    id BIGINT PRIMARY KEY AUTO_INCREMENT,
//    user_id BIGINT NOT NULL,
//    permission_id BIGINT NOT NULL,
//    is_allowed BOOLEAN NOT NULL,

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long permissionId;

    private Boolean isAllowed;
}
