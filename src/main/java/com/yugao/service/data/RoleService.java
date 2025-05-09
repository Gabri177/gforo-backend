package com.yugao.service.data;

import com.yugao.domain.permission.Role;
import com.yugao.enums.StatusEnum;

import java.util.List;

public interface RoleService {

    void addRole(Role role);
    Role getRoleById(Long id);
    String getRoleNameById(Long id);
    List<String> getRoleNamesByIds(List<Long> ids);
    List<Role> getAllRoles();
    Boolean deleteRoleById(Long id);
    Boolean changeRoleStatusById(Long id, StatusEnum status);
}
