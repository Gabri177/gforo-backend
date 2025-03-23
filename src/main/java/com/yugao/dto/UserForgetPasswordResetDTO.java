package com.yugao.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserForgetPasswordResetDTO {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 6, max = 20, message = "Username length must be between 6 and 20")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 20, message = "Password length must be between 6 and 20")
    private String password;
}
