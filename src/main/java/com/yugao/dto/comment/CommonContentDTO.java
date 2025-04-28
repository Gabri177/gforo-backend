package com.yugao.dto.comment;

import com.yugao.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommonContentDTO {

    @NotNull(message = "ID cannot be null", groups = ValidationGroups.DefaultGroup.class)
    private Long id;

    @NotBlank(message = "Title cannot be empty", groups = ValidationGroups.Post.class)
    @Size(min = 1, max = 50, message = "Title length must be between 1 and 50", groups = ValidationGroups.Post.class)
    private String title;

    @NotBlank(message = "Content cannot be empty", groups = ValidationGroups.DefaultGroup.class)
    @Size.List({
            @Size(min = 1, max = 5000, message = "Content length must be between 1 and 5000", groups = ValidationGroups.Post.class),
            @Size(min = 1, max = 500, message = "Content length must be between 1 and 500", groups = ValidationGroups.Comment.class)
    })
    private String content;
}
