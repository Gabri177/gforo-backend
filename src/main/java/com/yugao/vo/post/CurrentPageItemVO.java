package com.yugao.vo.post;

import com.yugao.domain.DiscussPost;
import com.yugao.vo.user.UserInfoVO;
import lombok.Data;

@Data
public class CurrentPageItemVO {

    private DiscussPost discussPosts;
    private UserInfoVO user;
    // 如果以后要加点赞数、评论数，在这里加字段
    private long likeCount;
}
