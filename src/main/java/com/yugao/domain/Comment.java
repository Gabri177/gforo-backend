package com.yugao.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("comment")
public class Comment {

//    id	int	NO	PRI		auto_increment
//    user_id	int	YES	MUL
//    entity_type	int	YES
//    entity_id	int	YES	MUL
//    target_id	int	YES
//    content	text	YES
//    status	int	YES
//    create_time	timestamp	YES

    private Long id; // 评论主键Id

    private Long userId; // 评论人Id

    private Integer entityType; // 针对帖子还是评论进行评论 0-针对帖子本身的评论 1-针对楼层包括帖子楼层的评论

    private Long entityId; // 评论的帖子Id或者评论的评论的Id

    private Long targetId; // 评论的目标Id，针对评论时为评论的Id，针对帖子时为0

    private String content; // 评论内容

    private Integer status; // 评论状态，0-正常，1-拉黑

    private Date createTime;

    private Long parentId; // 父评论Id，针对评论时为评论的Id，针对帖子时为0
}
