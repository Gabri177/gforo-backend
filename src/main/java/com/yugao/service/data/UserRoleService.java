package com.yugao.service.data;

import com.yugao.domain.UserRole;

import java.util.List;

public interface UserRoleService {

    Boolean addUserRole(UserRole userRole);
    Boolean changeUserRole(UserRole userRole);
    List<Long> getRoleIdsByUserId(Long userId);
    Boolean deleteUserRoleByUserId(Long userId);
    Boolean deleteUserRoleByRoleId(Long roleId);
}
