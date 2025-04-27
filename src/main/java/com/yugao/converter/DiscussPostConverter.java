package com.yugao.converter;


import com.yugao.domain.DiscussPost;
import com.yugao.dto.NewDiscussPostDTO;

import java.util.Date;

public class DiscussPostConverter {

    public static DiscussPost newDiscussPostDTOtoDiscussPost(NewDiscussPostDTO dto, Long userId){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(userId);
        discussPost.setTitle(dto.getTitle());
        discussPost.setContent(dto.getContent());
        discussPost.setStatus(0);
        discussPost.setType(0);
        discussPost.setCreateTime(new Date());
        discussPost.setCommentCount(0);
        discussPost.setScore(0.0);
        return discussPost;
    }
}
