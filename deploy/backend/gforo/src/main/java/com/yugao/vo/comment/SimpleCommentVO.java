package com.yugao.vo.comment;

import lombok.Data;

@Data
public class SimpleCommentVO {

    private Long id;
    private String content;
    private Long postId; // 帖子ID
}
