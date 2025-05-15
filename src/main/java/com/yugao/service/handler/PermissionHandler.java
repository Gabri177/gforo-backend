package com.yugao.service.handler;

import com.yugao.domain.permission.RolePermission;
import com.yugao.service.data.PermissionService;
import com.yugao.service.data.RolePermissionService;
import com.yugao.service.data.RoleService;
import com.yugao.service.data.UserRoleService;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PermissionHandler {

    private final UserRoleService userRoleService;
    private final RolePermissionService rolePermissionService;
    private final PermissionService permissionService;
    private final RoleService roleService;

    public List<String> getPermissionCodesByUserId(Long userId) {

        // 1. 获取用户的所有角色
        List<Long> roleIds = userRoleService.getRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) return List.of();

        // 2. 获取角色对应的所有权限id
        List<Long> permissionIds = rolePermissionService.getPermissionIdsByRoleIds(roleIds);
        if (permissionIds.isEmpty()) return List.of();

        // 3. 获取所有有效权限的code
        return permissionService.getCodesByIds(permissionIds);
    }

    public Boolean updateRolePermission(Long roleId, List<Long> permissionIds) {

        // 1. 删除角色的所有权限
        rolePermissionService.deleteRolePermissionsByRoleId(roleId);
        // 2. 添加角色的所有权限
        if (permissionIds != null && !permissionIds.isEmpty())
            rolePermissionService.addRolePermissions(
                    permissionIds.stream()
                            .map(item -> new RolePermission(roleId, item))
                            .toList()
            );
        return true;
    }

    public Integer getUserRoleLevel(Long userId) {

        List<Long> roleIds = userRoleService.getRoleIdsByUserId(userId);
        return roleService.getLowestRoleLevelByIds(roleIds);
    }
}
