package com.yugao.service;

import com.yugao.domain.UserToken;
import org.springframework.transaction.annotation.Transactional;

public interface UserTokenService {

    UserToken findByUserId(int userId);

    @Transactional
    Boolean saveUserToken(UserToken userToken);

    @Transactional
    Boolean deleteUserTokenByUserId(Long userId);

    @Transactional
    Boolean updateExpiresAt(Long userId, Long expiresAt);

    @Transactional
    Boolean updateAccessToken(Long userId, String accessToken);

    @Transactional
    Boolean updateRefreshToken(Long userId, String refreshToken);
}
