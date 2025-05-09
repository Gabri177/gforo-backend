package com.yugao.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yugao.domain.permission.UserRole;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.mapper.permission.UserRoleMapper;
import com.yugao.service.data.UserRoleService;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
    public Boolean deleteUserRole(Long userId, Long roleId) {

        LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRole::getUserId, userId);
        updateWrapper.eq(UserRole::getRoleId, roleId);
        return userRoleMapper.delete(updateWrapper) > 0;
    }

    @Override
    public Boolean deleteUserRole(UserRole userRole) {

        return deleteUserRole(userRole.getUserId(), userRole.getRoleId());
    }

    @Override
    public void deleteUserRolesByUserId(Long userId) {

        LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRole::getUserId, userId);
        userRoleMapper.delete(updateWrapper);
    }

    @Override
    public void addUserRoles(List<UserRole> userRoles) {

        List<BatchResult> results = userRoleMapper.insert(userRoles);

        // 总影响的行数
        long total = results.stream()
                .flatMapToInt(r -> Arrays.stream(r.getUpdateCounts()))
                .filter(i -> i == 1)
                .count();
        if (total != userRoles.size())
            throw new BusinessException(ResultCodeEnum.SQL_UPDATING_ERROR);
    }


}
