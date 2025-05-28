package com.yugao.dto.board;

import com.yugao.enums.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBoardDTO {

    @NotNull(message = "Board ID cannot be null")
    private Long id;

    @NotBlank(message = "Board name cannot be blank")
    private String name;

    @NotBlank(message = "Board description cannot be blank")
    private String description;

    @NotNull(message = "Board status cannot be null")
    private StatusEnum status;

    @NotBlank(message = "Icon URL cannot be blank")
    private String iconUrl;
}
