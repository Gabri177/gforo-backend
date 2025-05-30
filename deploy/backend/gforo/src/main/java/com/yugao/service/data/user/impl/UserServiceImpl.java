package com.yugao.service.data.user.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.domain.user.User;
import com.yugao.dto.user.UserInfoUpdateDTO;
import com.yugao.enums.StatusEnum;
import com.yugao.exception.BusinessException;
import com.yugao.mapper.user.UserMapper;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.service.data.user.UserService;
import com.yugao.vo.statistics.MonthlyUserStatsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Long getUserCount() {

        return userMapper.selectCount(null);
    }

    @Override
    public Long getUserCountWithLowerLevel(Long userId, Integer level) {

        return userMapper.countUsersWithLowerRoleLevel(userId, level);
    }

    @Override
    public Long getUserCountWithLowerLevel(Long userId, Integer level, String usernameKeyWord) {

        return userMapper.countUsersWithLowerRoleLevelAndUsernameLike(userId, level, usernameKeyWord);
    }

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
    public List<User> getUsers(Long id, Integer currentPage, Integer pageSize, Integer curUserLevel) {

        return userMapper.getUsersWithLowerRoleLevel(
                new Page<>(currentPage, pageSize),
                id,
                curUserLevel
        );
    }

    @Override
    public List<User> getUsers(Long userId, String usernameKeyWord, Integer currentPage, Integer pageSize, Integer curUserLevel) {

        return userMapper.getUsersWithLowerRoleLevelAndUsernameLike(
                new Page<>(currentPage, pageSize),
                userId,
                curUserLevel,
                usernameKeyWord
        );

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
    public boolean updateStatus(Long id, StatusEnum status) {
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

    @Override
    public Double getMonthGrowthRate() {

        LocalDateTime firstDayOfMonth = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime firstDayOfLastMonth = firstDayOfMonth.minusMonths(1);

        Long currentMonthCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .ge(User::getCreateTime, firstDayOfMonth)
                .lt(User::getCreateTime, LocalDateTime.now()));
        Long lastMonthCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .ge(User::getCreateTime, firstDayOfLastMonth)
                .lt(User::getCreateTime, firstDayOfMonth));

        double growthRate = 0.0;
        if (lastMonthCount != 0)
            growthRate = ((double) (currentMonthCount - lastMonthCount) / lastMonthCount) * 100;
        return growthRate;
    }

    @Override
    public List<MonthlyUserStatsVO> getMonthlyRegisterStats() {

        return userMapper.getMonthlyRegisterStats();
    }

    @Override
    public void increaseExp(Long userId, Integer exp) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId)
                .setSql("exp_points = exp_points + " + exp);
        userMapper.update(null, wrapper);
    }

    @Override
    public void decreaseExp(Long userId, Integer exp) {

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId)
                .setSql("exp_points = exp_points - " + exp);
        userMapper.update(null, wrapper);
    }


}
