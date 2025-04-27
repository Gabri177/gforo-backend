package com.yugao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class CommentToPostDTO {

    private Long entityId; // 评论的帖子Id

    @NotBlank(message = "Content cannot be empty")
    private String content; // 评论内容

}
