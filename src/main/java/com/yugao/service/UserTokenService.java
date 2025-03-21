package com.yugao.service;

import com.yugao.domain.UserToken;
import org.springframework.transaction.annotation.Transactional;

public interface UserTokenService {

    UserToken findByUserId(int userId);

    @Transactional
    Boolean saveUserToken(UserToken userToken);

    @Transactional
    Boolean deleteUserTokenByUserId(int userId);

    @Transactional
    Boolean updateExpiresAt(Integer userId, Long expiresAt);

    @Transactional
    Boolean updateAccessToken(Integer userId, String accessToken);

    @Transactional
    Boolean updateRefreshToken(Integer userId, String refreshToken);
}
