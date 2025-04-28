package com.yugao.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserForgetPasswordDTO {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 6, max = 20, message = "Username length must be between 6 and 20")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email format is incorrect")
    private String email;
}
