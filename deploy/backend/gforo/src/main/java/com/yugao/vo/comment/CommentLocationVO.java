package com.yugao.vo.comment;

import lombok.Data;

@Data
public class CommentLocationVO {

    private Long page; // 评论所在的页面位置

    private Boolean isPostFloor; // 是不是帖子楼层的评论

    private Long entityId; // 评论的实体ID

    private Long targetId; // 评论的目标ID
}
