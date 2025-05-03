package com.yugao.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenDTO {

    @NotBlank(message = "refresh token cannot be blank")
    private String refreshToken;
}
