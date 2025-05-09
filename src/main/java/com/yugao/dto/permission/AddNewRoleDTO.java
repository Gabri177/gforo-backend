package com.yugao.dto.permission;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddNewRoleDTO {

    @NotBlank(message = "Role name cannot be empty")
    private String name;

    @NotBlank(message = "Role description cannot be empty")
    private String description;

}
