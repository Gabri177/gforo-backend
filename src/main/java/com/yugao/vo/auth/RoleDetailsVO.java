package com.yugao.vo.auth;

import lombok.Data;

import java.util.List;

@Data
public class RoleDetailsVO {

    List<RoleDetailItemVO> roleDetailsList;

    List<SimplePermissionVO> permissionsList;
}
