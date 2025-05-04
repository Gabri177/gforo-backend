package com.yugao.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yugao.domain.RolePermission;
import com.yugao.mapper.RolePermissionMapper;
import com.yugao.service.data.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionImpl implements RolePermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public Boolean addRolePermission(RolePermission rolePermission) {

        return rolePermissionMapper.insert(rolePermission) > 0;
    }

    @Override
    public Boolean changeRolePermission(RolePermission rolePermission) {

        LambdaUpdateWrapper<RolePermission> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RolePermission::getRoleId, rolePermission.getRoleId())
                .set(RolePermission::getPermissionId, rolePermission.getPermissionId());
        return rolePermissionMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public List<Long> getPermissionIdsByRoleIds(List<Long> roleIds) {

        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RolePermission::getRoleId, roleIds);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(queryWrapper);
        return rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .toList();
    }

    @Override
    public Boolean deleteRolePermissionByRoleId(Long roleId) {

        LambdaUpdateWrapper<RolePermission> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RolePermission::getRoleId, roleId);
        return rolePermissionMapper.delete(updateWrapper) > 0;
    }

    @Override
    public Boolean deleteRolePermissionByPermissionId(Long permissionId) {

        LambdaUpdateWrapper<RolePermission> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RolePermission::getPermissionId, permissionId);
        return rolePermissionMapper.delete(updateWrapper) > 0;
    }
}
