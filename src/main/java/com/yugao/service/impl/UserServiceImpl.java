package com.yugao.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yugao.domain.User;
import com.yugao.mapper.UserMapper;
import com.yugao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
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
    public boolean updatePassword(Long id, String password) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, id).set(User::getPassword, password);
        return userMapper.update(null, wrapper) > 0;
    }
}
