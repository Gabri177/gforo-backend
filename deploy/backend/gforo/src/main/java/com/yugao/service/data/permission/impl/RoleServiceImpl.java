package com.yugao.service.data.permission.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yugao.domain.permission.Role;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.exception.BusinessException;
import com.yugao.mapper.permission.RoleMapper;
import com.yugao.service.data.permission.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;


    @Override
    public void addRole(Role role) {

        if (!(roleMapper.insert(role) > 0))
            throw new BusinessException(ResultCodeEnum.SQL_UPDATING_ERROR);
    }

    @Override
    public Role getRoleById(Long id) {

        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getId, id);
        return roleMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateRole(Role role) {

        roleMapper.updateById(role);
    }

    @Override
    public String getRoleNameById(Long id) {

        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getId, id);
        Role role = roleMapper.selectOne(queryWrapper);
        return role != null ? role.getName() : null;
    }

    @Override
    public List<String> getRoleNamesByIds(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return null;
        }
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Role::getId, ids);
        List<Role> roles = roleMapper.selectList(queryWrapper);
        return roles.stream()
                .map(Role::getName)
                .toList();
    }

    @Override
    public Integer getLowestRoleLevelByIds(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Role::getId, ids);
        List<Role> roles = roleMapper.selectList(queryWrapper);
        return roles.stream()
                .map(Role::getLevel)
                .min(Integer::compareTo)
                .orElse(0);
    }


    @Override
    public List<Role> getAllRoles() {

        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Role::getId);
        return roleMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean deleteRoleById(Long id) {

        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getId, id);
        return roleMapper.delete(queryWrapper) > 0;
    }

    @Override
    public Boolean changeRoleStatusById(Long id, StatusEnum status) {

        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getId, id);
        Role role = roleMapper.selectOne(queryWrapper);
        if (role != null) {
            role.setStatus(status);
            return roleMapper.updateById(role) > 0;
        }
        return false;
    }
}
