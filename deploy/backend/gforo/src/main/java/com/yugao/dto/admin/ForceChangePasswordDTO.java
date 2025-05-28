package com.yugao.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForceChangePasswordDTO {

    @NotBlank(message = "Password cannot be empty")
    private String newPassword;
}
