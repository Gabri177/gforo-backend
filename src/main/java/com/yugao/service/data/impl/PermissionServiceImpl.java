package com.yugao.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yugao.domain.Permission;
import com.yugao.mapper.PermissionMapper;
import com.yugao.service.data.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Boolean addPermission(Permission permission) {

        return permissionMapper.insert(permission) > 0;
    }

    @Override
    public Boolean deletePermissionById(Long id) {

        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getId, id);
        return permissionMapper.delete(queryWrapper) > 0;
    }

    @Override
    public String getCodeById(Long id) {

        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getId, id);
        Permission permission = permissionMapper.selectOne(queryWrapper);
        return permission != null ? permission.getCode() : null;
    }

    // 还没有判断权限状态 这个后面再考虑
    @Override
    public List<String> getCodesByIds(List<Long> ids) {

        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Permission::getId, ids);
        List<Permission> permissions = permissionMapper.selectList(queryWrapper);
        return permissions.stream()
                .map(Permission::getCode)
                .toList();
    }
}
