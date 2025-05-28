package com.yugao.dto.title;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddTitleDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;
}
