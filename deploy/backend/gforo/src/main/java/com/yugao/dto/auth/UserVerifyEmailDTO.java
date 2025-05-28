package com.yugao.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserVerifyEmailDTO {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email format is incorrect")
    private String email;
}
