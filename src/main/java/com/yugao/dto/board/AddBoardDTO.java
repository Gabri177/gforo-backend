package com.yugao.dto.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddBoardDTO {

    @NotBlank(message = "Board name cannot be blank")
    private String name;

    @NotBlank(message = "Board description cannot be blank")
    private String description;

    @NotBlank(message = "Icon URL cannot be blank")
    private String iconUrl;
}
