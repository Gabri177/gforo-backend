package com.yugao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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

    // 评论主键Id
    @TableId(type = IdType.AUTO)
    private Long id;

    // 评论人Id
    private Long userId;

    // entityType = 0, entityId = 帖子Id, targetId = 0 针对帖子的评论
    // entityType = 1, entityId = 帖子Id, targetId = 0 针对帖子楼层的评论
    // entityType = 1, entityId = 帖子Id, targetId = 其他用户Id 针对帖子的评论的评论且带有@对象
    // entityType = 2, entityId = 评论Id, targetId = 0 针对帖子的评论的评论
    // entityType = 2, entityId = 评论Id, targetId = 其他用户Id 针对帖子的评论的评论且带@对象
    private Integer entityType;

    // 评论的帖子Id或者评论的评论的Id
    private Long entityId;

    // 评论的目标Id，针对评论时为评论的Id，针对帖子时为0
    private Long targetId;

    // 评论内容
    private String content;

    // 评论状态，0-正常，1-拉黑
    private Integer status;

    private Date createTime;

    // 目前没有用到
    private Long parentId;
}
