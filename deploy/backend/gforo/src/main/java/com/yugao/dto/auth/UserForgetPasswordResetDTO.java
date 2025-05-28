package com.yugao.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserForgetPasswordResetDTO {

    @NotBlank(message = "email cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 20, message = "Password length must be between 6 and 20")
    private String password;

    @NotBlank(message = "Verification code cannot be empty")
    private String symbol;
}
