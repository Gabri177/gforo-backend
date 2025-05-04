package com.yugao.service.business.security.impl;

import com.yugao.domain.Permission;
import com.yugao.service.business.security.PermissionBusinessService;
import com.yugao.service.data.PermissionService;
import com.yugao.service.data.RolePermissionService;
import com.yugao.service.data.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionBusinessServiceImpl implements PermissionBusinessService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private PermissionService permissionService;

    @Override
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
}
