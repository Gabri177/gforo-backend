package com.yugao.vo.post;

import lombok.Data;

import java.util.Date;

@Data
public class SimpleDiscussPostVO {

    private Long id; // 帖子Id

    private String title;

    private Integer type; // 帖子类型，0-普通，1-置顶 2-精华

    private Integer status; // 帖子状态，0-正常，1-删除

    private Date createTime;

    private Double score;
}
