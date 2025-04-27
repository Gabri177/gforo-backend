package com.yugao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class CommentToCommentDTO {

    private Integer entityType; // 评论类型

    private Long entityId; // 评论的帖子Id 或者 评论的评论的Id

    private Long targetId; // 评论的目标Id，针对评论时为评论的Id，针对帖子时为0

    @NotBlank(message = "Content cannot be empty")
    private String content; // 评论内容
}
