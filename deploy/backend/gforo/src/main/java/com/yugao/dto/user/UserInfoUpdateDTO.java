package com.yugao.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserInfoUpdateDTO {

    @NotBlank(message = "Nickname cannot be empty")
    @Size(min = 1, max = 10, message = "Nickname length must be between 1 and 10")
    private String nickname;

    @NotBlank(message = "Avatar cannot be empty")
    private String headerUrl;

    private String bio;
}
