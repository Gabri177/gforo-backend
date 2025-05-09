package com.yugao.service.handler;

import com.yugao.domain.permission.RolePermission;
import com.yugao.dto.permission.UpdateRolePermissionDTO;
import com.yugao.service.data.PermissionService;
import com.yugao.service.data.RolePermissionService;
import com.yugao.service.data.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PermissionHandler {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private PermissionService permissionService;


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

    @Transactional
    public Boolean updateRolePermission(UpdateRolePermissionDTO updateRolePermissionDTO) {

        // 1. 删除角色的所有权限
        rolePermissionService.deleteRolePermissionsByRoleId(updateRolePermissionDTO.getRoleId());
        // 2. 添加角色的所有权限
        if (updateRolePermissionDTO.getPermissionIds() != null &&
                !updateRolePermissionDTO.getPermissionIds().isEmpty())
            rolePermissionService.addRolePermissions(
                    updateRolePermissionDTO.getPermissionIds()
                            .stream()
                            .map(item -> new RolePermission(updateRolePermissionDTO.getRoleId(), item))
                            .toList()
            );
        return true;
    }
}
