package com.yugao.service.data.title.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yugao.domain.title.UserTitle;
import com.yugao.mapper.title.UserTitleMapper;
import com.yugao.service.data.title.UserTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTitleServiceImpl implements UserTitleService {

    private final UserTitleMapper userTitleMapper;

    @Override
    public void addUserTitle(UserTitle userTitle) {

        userTitleMapper.insert(userTitle);
    }

    @Override
    public void addUserTitle(Long userId, Long titleId) {

        UserTitle userTitle = new UserTitle();
        userTitle.setUserId(userId);
        userTitle.setTitleId(titleId);
        userTitle.setGainTime(LocalDateTime.now());
        userTitleMapper.insert(userTitle);
    }

    @Override
    public Long getUserTitlesCount(Long userId) {

        LambdaQueryWrapper<UserTitle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTitle::getUserId, userId);

        return userTitleMapper.selectCount(queryWrapper);
    }

    @Override
    public List<Long> getTitleIdsByUserId(Long userId) {

        LambdaQueryWrapper<UserTitle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTitle::getUserId, userId);

        return userTitleMapper.selectList(queryWrapper)
                .stream()
                .map(UserTitle::getTitleId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserTitleByTitleId(Long titleId) {

        LambdaQueryWrapper<UserTitle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTitle::getTitleId, titleId);
        userTitleMapper.delete(queryWrapper);
    }
}
