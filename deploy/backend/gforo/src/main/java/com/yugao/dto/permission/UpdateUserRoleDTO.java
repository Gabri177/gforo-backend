package com.yugao.dto.permission;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRoleDTO {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Role ID cannot be null")
    private List<Long> roleIds;

    private List<Long> boardIds;
}
