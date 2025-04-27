package com.yugao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class NewDiscussPostDTO {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Content cannot be empty")
    private String content;

}
