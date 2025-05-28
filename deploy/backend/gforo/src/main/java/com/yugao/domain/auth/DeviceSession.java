package com.yugao.domain.auth;

import lombok.Data;

@Data
public class DeviceSession {

    private String deviceId;
    private String accessToken;
    private String refreshToken;
    private Long loginTimestamp; // 登录时间
    private Long accessExpireTimestamp; // accessToken 过期时间
    private Long refreshExpireTimestamp; // refreshToken 过期时间
}
