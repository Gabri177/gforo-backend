package com.yugao.vo;

import com.yugao.domain.DiscussPost;
import lombok.Data;

@Data
public class CurrentPageItemVO {

    private DiscussPost discussPosts;
    private UserInfoVO user;
    // 如果以后要加点赞数、评论数，在这里加字段
    private long likeCount;
}
