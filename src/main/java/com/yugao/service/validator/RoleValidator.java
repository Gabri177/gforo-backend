package com.yugao.service.validator;

import com.yugao.domain.permission.Role;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.service.data.RoleService;
import com.yugao.service.handler.PermissionHandler;
import com.yugao.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleValidator {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionHandler permissionHandler;

    public void checkRoleIds(List<Long> roleIds) {
        List<Long> allRoleIds = roleService.getAllRoles()
                .stream()
                .map(Role::getId)
                .toList();
        if (roleIds.stream().noneMatch(allRoleIds::contains))
            throw new BusinessException(ResultCodeEnum.ROLE_NOT_EXISTS);
    }

    public void checkRoleId (Long roleId) {

        Role role = roleService.getRoleById(roleId);
        if (role == null)
            throw new BusinessException(ResultCodeEnum.ROLE_NOT_EXISTS);
    }

    public void checkRoleName(String roleName){

        List<Role> allRoles = roleService.getAllRoles();
        long repeatCount = allRoles.stream()
                .filter(role -> role.getName().equals(roleName))
                .count();
        if (repeatCount > 0)
            throw new BusinessException(ResultCodeEnum.ROLE_NAME_EXISTS);
    }


    // level 越小权限越高  检查目标权限是不是比当前用户权限高
    public void checkLevel(Integer tarLev) {

        Integer curlev = SecurityUtils.getUserLevel();
        if (tarLev <= curlev)
            throw new BusinessException(ResultCodeEnum.ROLE_LEVEL_NOT_ENOUGH);
    }
}
