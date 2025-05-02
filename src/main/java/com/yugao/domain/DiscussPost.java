package com.yugao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yugao.enums.StatusEnum;
import lombok.Data;

import java.util.Date;

@Data
@TableName("discuss_post")
public class DiscussPost {
    //`id` INT(11) NOT NULL AUTO_INCREMENT,
    //`user_id` VARCHAR(45) DEFAULT NULL,
    //`title` VARCHAR(100) DEFAULT NULL,
    //`content` TEXT,
    //`type` INT(11) DEFAULT NULL COMMENT '0-普通 1-置顶',
    //`status` INT(11) DEFAULT NULL COMMENT '0-正常 1-精华 2-拉黑',
    //`create_time` TIMESTAMP NULL DEFAULT NULL,
    //`comment_count` int(11) DEFAULT NULL,
    //`score` DOUBLE DEFAULT NULL,

    @TableId(type = IdType.AUTO)
    private Long id; // 帖子Id

    private Long userId; // 发帖人Id

    private String title;

    private String content;

    private Integer type; // 帖子类型，0-普通，1-置顶 2-精华

    private StatusEnum status; // 帖子状态，0-正常, 1-删除

    private Date createTime;

    private Double score;

    private Long boardId; // 板块Id
}
