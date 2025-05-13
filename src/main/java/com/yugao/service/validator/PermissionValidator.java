package com.yugao.service.validator;

import com.yugao.domain.permission.Permission;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.service.data.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionValidator {

    @Autowired
    private PermissionService permissionService;

    public void checkPermissionIds(List<Long> permissionIds) {

        if (permissionIds == null || permissionIds.isEmpty())
            return ;

        List<Permission> allPermissions = permissionService.getAllPermissions();
        long validCount = allPermissions.stream()
                .mapToLong(Permission::getId)
                .filter(permissionIds::contains)
                .count();
        if (validCount != permissionIds.size())
            throw new BusinessException(ResultCodeEnum.PERMISSION_NOT_EXISTS);
    }
}
