package com.yugao.service.data.permission;

import com.yugao.domain.permission.Permission;

import java.util.List;

public interface PermissionService {

    Boolean addPermission(Permission permission);

    Boolean deletePermissionById(Long id);

    String getCodeById(Long id);

    List<String> getCodesByIds(List<Long> ids);

    List<Permission> getPermissionsByIds(List<Long> ids);

    List<Permission> getAllPermissions();
}
