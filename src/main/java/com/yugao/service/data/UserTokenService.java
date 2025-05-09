package com.yugao.service.data;

import com.yugao.domain.auth.UserToken;

public interface UserTokenService {

    UserToken findByUserId(Long userId);

    Boolean saveUserToken(UserToken userToken);

    Boolean deleteUserTokenByUserId(Long userId);

    Boolean updateExpiresAt(Long userId, Long expiresAt);

    Boolean updateAccessToken(Long userId, String accessToken);

    Boolean updateRefreshToken(Long userId, String refreshToken);
}
