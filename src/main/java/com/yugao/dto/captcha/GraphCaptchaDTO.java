package com.yugao.dto.captcha;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GraphCaptchaDTO {

    @NotBlank(message = "Verification code cannot be empty")
    private String verCode; //code

    @NotBlank(message = "Verification code ID cannot be empty")
    private String captchaId; //验证码ID

    @NotBlank(message = "Username cannot be empty")
    private String scene; //场景

    @NotBlank(message = "symbol cannot be empty")
    private String symbol; //标志位  通过场景和标志位确定验证码key的唯一性
}
