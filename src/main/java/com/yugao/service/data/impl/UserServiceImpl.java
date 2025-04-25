package com.yugao.service.data.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yugao.domain.User;
import com.yugao.dto.UserInfoUpdateDTO;
import com.yugao.exception.BusinessException;
import com.yugao.mapper.UserMapper;
import com.yugao.result.ResultCode;
import com.yugao.service.data.UserService;
import com.yugao.util.common.EncryptedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public boolean updateEmail(Long id, String email) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, id)
                .set(User::getEmail, email)
                .set(User::getStatus, 0)
                .set(User::getActivationCode, EncryptedUtil.generateUUID()
                );
        return userMapper.update(null, wrapper) > 0;
    }

    @Transactional
    @Override
    public void updateUserProfile(Long id, UserInfoUpdateDTO userInfoUpdateDTO) {
        // 查询当前用户
        User user = getUserById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 更新基本字段
        user.setUsername(userInfoUpdateDTO.getUsername());
        user.setBio(userInfoUpdateDTO.getBio());
        user.setHeaderUrl(userInfoUpdateDTO.getHeaderUrl());
        updateUser(user);

        // 更新邮箱（包含 status 和 activationCode 的刷新）
        if (!user.getEmail().equals(userInfoUpdateDTO.getEmail())) {
            updateEmail(id, userInfoUpdateDTO.getEmail());
        }
    }
}
