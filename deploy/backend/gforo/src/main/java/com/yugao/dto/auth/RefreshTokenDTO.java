package com.yugao.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenDTO {

    @NotBlank(message = "refresh token cannot be blank")
    private String refreshToken;

    @NotBlank(message = "device id cannot be blank")
    private String symbol; // 设备标识符
}
