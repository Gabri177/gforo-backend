package com.yugao.dto.permission;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRolePermissionDTO {

    @NotNull(message = "Role ID cannot be null")
    private Long roleId;

    private List<Long> permissionIds;
}
