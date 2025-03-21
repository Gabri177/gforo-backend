package com.yugao.service;

import com.yugao.domain.UserToken;
import org.springframework.transaction.annotation.Transactional;

public interface UserTokenService {

    public UserToken findByUserId(int userId);

    @Transactional
    public Boolean saveUserToken(UserToken userToken);

    @Transactional
    public Boolean deleteUserTokenByUserId(int userId);

    @Transactional
    public Boolean updateExpiresAt(Integer userId, Long expiresAt);

    @Transactional
    public Boolean updateAccessToken(Integer userId, String accessToken);

    @Transactional
    public Boolean updateRefreshToken(Integer userId, String refreshToken);
}
