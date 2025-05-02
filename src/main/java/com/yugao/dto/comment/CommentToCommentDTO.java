package com.yugao.dto.comment;

import com.yugao.enums.CommentEntityTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentToCommentDTO {

    @NotNull(message = "EntityType cannot be null")
    private CommentEntityTypeEnum entityType; // 评论类型

    @NotNull(message = "EntityId cannot be null")
    private Long entityId; // 评论的帖子Id 或者 评论的评论的Id

    private Long targetId; // 评论的目标Id，针对评论时为评论的Id，针对帖子时为0

    @NotBlank(message = "Content cannot be empty")
    @Size(min = 1, max = 500, message = "Content length must be between 1 and 500")
    private String content; // 评论内容
}
