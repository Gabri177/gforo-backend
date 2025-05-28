package com.yugao.service.validator;

import com.yugao.domain.permission.Role;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.security.LoginUser;
import com.yugao.service.data.permission.RoleService;
import com.yugao.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleValidator {

    private final RoleService roleService;

    public void checkRoleIds(List<Long> roleIds) {
        List<Long> allRoleIds = roleService.getAllRoles()
                .stream()
                .map(Role::getId)
                .toList();
        if (roleIds.stream().noneMatch(allRoleIds::contains))
            throw new BusinessException(ResultCodeEnum.ROLE_NOT_EXISTS);
    }

    public Role checkRoleId (Long roleId) {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        Role role = roleService.getRoleById(roleId);
        if (loginUser.getIsAdmin() && role.getBuildin() == 1)
            throw new BusinessException(ResultCodeEnum.BUILDIN_ROLE_NOT_MODIFIABLE);
        if (role == null)
            throw new BusinessException(ResultCodeEnum.ROLE_NOT_EXISTS);
        return role;
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
