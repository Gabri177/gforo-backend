package com.yugao.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yugao.domain.UserToken;
import com.yugao.mapper.UserTokensMapper;
import com.yugao.service.data.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    private UserTokensMapper userTokensMapper;

    @Override
    public UserToken findByUserId(Long userId) {
        LambdaUpdateWrapper<UserToken> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserToken::getUserId, userId);
        return userTokensMapper.selectOne(wrapper);
    }

    @Override
    public Boolean saveUserToken(UserToken userToken) {
        return userTokensMapper.insert(userToken) > 0;
    }

    @Override
    public Boolean deleteUserTokenByUserId(Long userId) {
        LambdaUpdateWrapper<UserToken> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserToken::getUserId, userId);
        return userTokensMapper.delete(wrapper) > 0;
    }

    @Override
    public Boolean updateExpiresAt(Long userId, Long expiresAt) {
        LambdaUpdateWrapper<UserToken> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserToken::getUserId, userId);
        wrapper.set(UserToken::getExpiresAt, expiresAt);
        return userTokensMapper.update(wrapper) > 0;
    }

    @Override
    public Boolean updateAccessToken(Long userId, String accessToken) {
        LambdaUpdateWrapper<UserToken> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserToken::getUserId, userId);
        wrapper.set(UserToken::getAccessToken, accessToken);
        return userTokensMapper.update(wrapper) > 0;
    }

    @Override
    public Boolean updateRefreshToken(Long userId, String refreshToken) {
        LambdaUpdateWrapper<UserToken> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserToken::getUserId, userId);
        wrapper.set(UserToken::getRefreshToken, refreshToken);
        return userTokensMapper.update(wrapper) > 0;
    }
}
