package com.yugao.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yugao.domain.permission.RolePermission;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.mapper.permission.RolePermissionMapper;
import com.yugao.service.data.RolePermissionService;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
    public List<Long> getPermissionIdsByRoleId(Long roleId) {

        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(queryWrapper);
        return rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .toList();
    }

    @Override
    public void deleteRolePermission(Long roleId, Long permissionId) {

        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId, roleId)
                .eq(RolePermission::getPermissionId, permissionId);
        rolePermissionMapper.delete(queryWrapper);
    }

    @Override
    public void deleteRolePermissionsByRoleId(Long roleId) {

        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(queryWrapper);
    }

    @Override
    public Boolean addRolePermissions(List<RolePermission> rolePermissions) {

        List<BatchResult> results = rolePermissionMapper.insert(rolePermissions);

        long total = results.stream()
                .flatMapToInt(res -> Arrays.stream(res.getUpdateCounts()))
                .filter(i -> i == 1)
                .count();
        if (total != rolePermissions.size())
            throw new BusinessException(ResultCodeEnum.SQL_UPDATING_ERROR);
        return true;
    }

}
