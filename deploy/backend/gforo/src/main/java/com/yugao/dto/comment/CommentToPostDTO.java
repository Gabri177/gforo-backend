package com.yugao.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentToPostDTO {

    @NotNull(message = "ID cannot be null")
    private Long entityId; // 评论的帖子Id

    @NotBlank(message = "Content cannot be empty")
    @Size(min = 1, max = 2000, message = "Content length must be between 1 and 2000")
    private String content; // 评论内容

    @NotNull(message = "toPostUserId cannot be null")
    private Long toPostUserId;

}
