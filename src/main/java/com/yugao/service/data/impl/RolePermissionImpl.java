package com.yugao.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yugao.domain.RolePermission;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.mapper.RolePermissionMapper;
import com.yugao.service.data.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Boolean deleteRolePermission(Long roleId, Long permissionId) {

        LambdaUpdateWrapper<RolePermission> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RolePermission::getRoleId, roleId)
                .eq(RolePermission::getPermissionId, permissionId);
        return rolePermissionMapper.delete(updateWrapper) > 0;
    }

    @Override
    public Boolean deleteRolePermissions(Long roleId, List<Long> permissionIds) {

        LambdaUpdateWrapper<RolePermission> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RolePermission::getRoleId, roleId)
                .in(RolePermission::getPermissionId, permissionIds);
        return rolePermissionMapper.delete(updateWrapper) > 0;
    }

    @Transactional
    @Override
    public Boolean addRolePermissions(Long roleId, List<Long> permissionIds) {

        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission(roleId, permissionId);
            if (!addRolePermission(rolePermission))
                throw new BusinessException(ResultCodeEnum.SQL_UPDATING_ERROR);
        }
        return true;
    }

}
