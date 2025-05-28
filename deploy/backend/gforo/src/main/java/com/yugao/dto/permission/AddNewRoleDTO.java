package com.yugao.dto.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AddNewRoleDTO {

    @NotBlank(message = "Role name cannot be empty")
    private String roleName;

    @NotBlank(message = "Role description cannot be empty")
    private String roleDescription;

    private List<Long> permissionIds;

    @NotNull(message = "Role level cannot be empty")
    private Integer roleLevel;

}
