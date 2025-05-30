package com.yugao.dto.auth;

import com.yugao.validation.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {

    @NotBlank(message = "Username cannot be empty", groups = {ValidationGroups.DefaultGroup.class})
    @Size(min = 6, max = 20, message = "Username length must be between 6 and 20", groups = {ValidationGroups.DefaultGroup.class})
    private String username;

    @NotBlank(message = "Password cannot be empty", groups = {ValidationGroups.DefaultGroup.class})
    @Size(min = 6, max = 20, message = "Password length must be between 6 and 20", groups = {ValidationGroups.DefaultGroup.class})
    private String password;

    @NotBlank(message = "Email cannot be empty", groups = {ValidationGroups.Register.class})
    @Email(message = "Email format is incorrect", groups = {ValidationGroups.Register.class})
    private String email;

    @NotBlank(message = "Avatar cannot be empty", groups = {ValidationGroups.Register.class})
    private String headerUrl;

    @NotBlank(message = "Verification code cannot be empty", groups = {ValidationGroups.DefaultGroup.class})
    private String symbol; //标志位  确定验证码key的唯一性
}
