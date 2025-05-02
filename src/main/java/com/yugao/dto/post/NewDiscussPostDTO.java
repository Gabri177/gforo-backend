package com.yugao.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class NewDiscussPostDTO {

    @NotNull(message = "Board ID cannot be empty")
    private Long boardId;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 1, max = 50, message = "Title length must be between 1 and 50")
    private String title;

    @NotBlank(message = "Content cannot be empty")
    @Size(min = 1, max = 5000, message = "Content length must be between 1 and 5000")
    private String content;

}
