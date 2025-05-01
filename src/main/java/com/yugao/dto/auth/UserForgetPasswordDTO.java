package com.yugao.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserForgetPasswordDTO {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email format is incorrect")
    private String email;

    @NotBlank(message = "Verification code cannot be empty")
    private String symbol;
}
