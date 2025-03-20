package com.yugao.domain;

import com.baomidou.mybatisplus.annotation.TableName;
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

    private Integer id;

    private Integer userId;

    private String title;

    private String content;

    private Integer type;

    private Integer status;

    private Date createTime;

    private Integer commentCount;

    private Double score;
}
