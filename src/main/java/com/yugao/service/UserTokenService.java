package com.yugao.service;

import com.yugao.domain.UserToken;

public interface UserTokenService {

    UserToken findByUserId(int userId);

    Boolean saveUserToken(UserToken userToken);

    Boolean deleteUserTokenByUserId(Long userId);

    Boolean updateExpiresAt(Long userId, Long expiresAt);

    Boolean updateAccessToken(Long userId, String accessToken);

    Boolean updateRefreshToken(Long userId, String refreshToken);
}
