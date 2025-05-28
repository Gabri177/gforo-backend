package com.yugao.vo.auth;

import com.yugao.enums.StatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RoleDetailItemVO {

    private Long id;

    private String name;

    private String description;

    private StatusEnum status;

    private Date createTime;

    private Integer level;

    private Integer buildin; // 是否内置角色 0:否 1:是

    private List<SimplePermissionVO> permissions;
}
