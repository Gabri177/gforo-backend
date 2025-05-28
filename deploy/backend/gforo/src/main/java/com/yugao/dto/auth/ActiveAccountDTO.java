package com.yugao.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActiveAccountDTO {

    @NotBlank(message = "Captcha cannot be empty")
    private String sixDigitCaptcha;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
}
