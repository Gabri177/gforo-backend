package com.yugao.dto.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class UpdateRolePermissionDTO {

    @NotNull(message = "Role ID cannot be null")
    private Long roleId;

    @NotBlank(message = "Role name cannot be blank")
    private String roleName;

    @NotBlank(message = "Role description cannot be blank")
    private String roleDescription;

    @NotNull(message = "Role level cannot be null")
    private Integer roleLevel;

    private List<Long> permissionIds;
}
