package com.yugao.service.data;

import com.yugao.domain.permission.RolePermission;

import java.util.List;

public interface RolePermissionService {

    Boolean addRolePermission(RolePermission rolePermission);
    Boolean changeRolePermission(RolePermission rolePermission);
    List<Long> getPermissionIdsByRoleIds(List<Long> roleIds);
    List<Long> getPermissionIdsByRoleId(Long roleId);
    void deleteRolePermission(Long roleId, Long permissionId);
    void deleteRolePermissionsByRoleId(Long roleId);
    Boolean addRolePermissions(List<RolePermission> rolePermissions);
}
