package com.yugao.dto;

import com.yugao.validation.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserInfoUpdateDTO {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 6, max = 20, message = "Username length must be between 6 and 20")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email format is incorrect")
    private String email;

    @NotBlank(message = "Avatar cannot be empty")
    private String headerUrl;

    private String bio;
}
