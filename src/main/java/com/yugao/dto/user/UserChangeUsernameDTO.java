package com.yugao.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserChangeUsernameDTO {

    @NotBlank(message = "new username cannot be empty")
    @Size(min = 6, max = 20, message = "Username length must be between 6 and 20")
    private String username;
}
