package com.yugao.service.data;

import com.yugao.domain.Permission;

import java.util.List;

public interface PermissionService {

    Boolean addPermission(Permission permission);

    Boolean deletePermissionById(Long id);

    String getCodeById(Long id);

    List<String> getCodesByIds(List<Long> ids);

    List<Permission> getPermissionsByIds(List<Long> ids);
}
