package com.yugao.service.data.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yugao.domain.User;
import com.yugao.dto.user.UserInfoUpdateDTO;
import com.yugao.exception.BusinessException;
import com.yugao.mapper.UserMapper;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.service.data.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getUsersByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty())
            return Collections.emptyList();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(User::getId, ids);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public User getUserByName(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User getUserByEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean addUser(User user) {
        return userMapper.insert(user) > 0;
    }

    @Override
    public boolean updateStatus(Long id, int status) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, id).set(User::getStatus, status);
        return userMapper.update(null, wrapper) > 0;
    }

    @Override
    public boolean updateHeader(Long id, String headerUrl) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, id).set(User::getHeaderUrl, headerUrl);
        return userMapper.update(null, wrapper) > 0;
    }

    @Override
    public boolean updateUsername(Long id, String username) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, id)
                .set(User::getUsername, username)
                .set(User::getLastUsernameUpdateTime, new Date());
        return userMapper.update(null, wrapper) > 0;
    }

    @Override
    public boolean updatePassword(Long id, String password) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, id).set(User::getPassword, password);
        return userMapper.update(null, wrapper) > 0;
    }

    @Override
    public boolean updateEmail(Long id, String email) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, id)
                .set(User::getEmail, email)
                .set(User::getLastEmailUpdateTime, new Date());
        return userMapper.update(null, wrapper) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        return getUserByEmail(email) != null;
    }

    @Override
    public boolean existsByUsername(String username) {
        return getUserByName(username) != null;
    }

    @Override
    public boolean updateUserProfile(Long id, UserInfoUpdateDTO userInfoUpdateDTO) {
        // 查询当前用户
        User user = getUserById(id);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_FOUND);
        }
        // 更新用户信息
        BeanUtils.copyProperties(userInfoUpdateDTO, user);
        return updateUser(user);
    }

    @Override
    public Date getLastUsernameUpdateTime(Long id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, id);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_FOUND);
        }
        return user.getLastUsernameUpdateTime();
    }

    @Override
    public Date getLastEmailUpdateTime(Long id) {

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, id);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_FOUND);
        }
        return user.getLastEmailUpdateTime();
    }
}
