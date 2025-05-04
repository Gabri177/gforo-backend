package com.yugao.service.data;

import com.yugao.domain.RolePermission;

import java.util.List;

public interface RolePermissionService {

    Boolean addRolePermission(RolePermission rolePermission);
    Boolean changeRolePermission(RolePermission rolePermission);
    List<Long> getPermissionIdsByRoleIds(List<Long> roleIds);
    Boolean deleteRolePermissionByRoleId(Long roleId);
    Boolean deleteRolePermissionByPermissionId(Long permissionId);
}
