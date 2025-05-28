package com.yugao.service.data.permission;

import com.yugao.domain.permission.UserRole;

import java.util.List;

public interface UserRoleService {

    Boolean addUserRole(UserRole userRole);
    Boolean changeUserRole(UserRole userRole);
    List<Long> getRoleIdsByUserId(Long userId);
    Boolean deleteUserRole(Long userId, Long roleId);
    Boolean deleteUserRole(UserRole userRole);
    void deleteUserRolesByUserId(Long userId);
    void deleteUserRolesByRoleId(Long roleId);
    void addUserRoles(List<UserRole> userRoles);
}
