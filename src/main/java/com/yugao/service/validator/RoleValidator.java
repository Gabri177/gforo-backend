package com.yugao.service.validator;

import com.yugao.domain.permission.Role;
import com.yugao.dto.permission.AddNewRoleDTO;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.service.data.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleValidator {

    @Autowired
    private RoleService roleService;

    public void checkRoleIds(List<Long> roleIds) {
        List<Long> allRoleIds = roleService.getAllRoles()
                .stream()
                .map(Role::getId)
                .toList();
        if (roleIds.stream().noneMatch(allRoleIds::contains))
            throw new BusinessException(ResultCodeEnum.ROLE_NOT_EXISTS);
    }

    public void checkNewRole(AddNewRoleDTO dto){

        List<Role> allRoles = roleService.getAllRoles();
        long repeatCount = allRoles.stream()
                .filter(role -> role.getName().equals(dto.getName()))
                .count();
        if (repeatCount > 0)
            throw new BusinessException(ResultCodeEnum.ROLE_NAME_EXISTS);
    }
}
