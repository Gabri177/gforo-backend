package com.yugao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class CommentToPostDTO {

    @NotNull(message = "ID cannot be null")
    private Long entityId; // 评论的帖子Id

    @NotBlank(message = "Content cannot be empty")
    @Size(min = 1, max = 500, message = "Content length must be between 1 and 500")
    private String content; // 评论内容

}
