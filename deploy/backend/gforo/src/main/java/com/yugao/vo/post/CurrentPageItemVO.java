package com.yugao.vo.post;

import com.yugao.vo.user.SimpleUserVO;
import lombok.Data;

@Data
public class CurrentPageItemVO {

    private SimpleDiscussPostVO discussPosts;

    private SimpleUserVO user;

    // 如果以后要加点赞数、评论数，在这里加字段
    private Integer likeCount;

    private Long commentCount;
}
