package com.yugao.converter;

import com.yugao.domain.permission.Role;
import com.yugao.dto.permission.AddNewRoleDTO;
import com.yugao.dto.permission.UpdateRolePermissionDTO;
import com.yugao.enums.StatusEnum;
import lombok.Data;

import java.util.Date;

@Data
public class RoleConverter {

    public static Role toRole(AddNewRoleDTO dto) {
        Role role = new Role();
        role.setName(dto.getRoleName());
        role.setDescription(dto.getRoleDescription());
        role.setStatus(StatusEnum.NORMAL);
        role.setCreateTime(new Date());
        role.setLevel(dto.getRoleLevel());
        return role;
    }

    public static Role toRole(UpdateRolePermissionDTO dto){
        Role role = new Role();
        role.setId(dto.getRoleId());
        role.setName(dto.getRoleName());
        role.setDescription(dto.getRoleDescription());
        role.setLevel(dto.getRoleLevel());
        return role;
    }
}
