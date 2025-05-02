package com.yugao.converter;


import com.yugao.domain.DiscussPost;
import com.yugao.dto.post.NewDiscussPostDTO;
import com.yugao.enums.StatusEnum;

import java.util.Date;

public class DiscussPostConverter {

    public static DiscussPost newDiscussPostDTOtoDiscussPost(NewDiscussPostDTO dto, Long userId){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(userId);
        discussPost.setTitle(dto.getTitle());
        discussPost.setContent(dto.getContent());
        discussPost.setStatus(StatusEnum.NORMAL);
        discussPost.setType(0);
        discussPost.setCreateTime(new Date());
        discussPost.setScore(0.0);
        discussPost.setBoardId(dto.getBoardId());
        return discussPost;
    }
}
