package com.yugao.vo.auth;

import com.yugao.domain.Permission;
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

    private List<SimplePermissionVO> permissions;
}
