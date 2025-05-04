package com.yugao.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yugao.domain.UserRole;
import com.yugao.mapper.UserRoleMapper;
import com.yugao.service.data.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;


    @Override
    public Boolean addUserRole(UserRole userRole) {

        return userRoleMapper.insert(userRole) > 0;
    }

    @Override
    public Boolean changeUserRole(UserRole userRole) {

        LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRole::getUserId, userRole.getUserId());
        updateWrapper.set(UserRole::getRoleId, userRole.getRoleId());
        return userRoleMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId);
        List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper);
        return userRoles.stream()
                .map(UserRole::getRoleId)
                .toList();
    }

    @Override
    public Boolean deleteUserRoleByUserId(Long userId) {

        LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRole::getUserId, userId);
        return userRoleMapper.delete(updateWrapper) > 0;
    }

    @Override
    public Boolean deleteUserRoleByRoleId(Long roleId) {

        LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRole::getRoleId, roleId);
        return userRoleMapper.delete(updateWrapper) > 0;
    }
}
