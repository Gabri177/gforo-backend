package com.yugao.service.data;

import com.yugao.domain.RolePermission;

import java.util.List;

public interface RolePermissionService {

    Boolean addRolePermission(RolePermission rolePermission);
    Boolean changeRolePermission(RolePermission rolePermission);
    List<Long> getPermissionIdsByRoleIds(List<Long> roleIds);
    List<Long> getPermissionIdsByRoleId(Long roleId);
    Boolean deleteRolePermission(Long roleId, Long permissionId);
    Boolean deleteRolePermissions(Long roleId, List<Long> permissionIds);
    Boolean addRolePermissions(Long roleId, List<Long> permissionIds);
}
