package com.yugao.service.validator;

import com.yugao.domain.permission.Permission;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.service.data.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PermissionValidator {

    private final PermissionService permissionService;

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
