package com.yugao.converter;


import com.yugao.domain.post.DiscussPost;
import com.yugao.dto.post.NewDiscussPostDTO;
import com.yugao.enums.StatusEnum;
import com.yugao.vo.post.SimpleDiscussPostVO;

import java.util.Date;

public class DiscussPostConverter {

    public static DiscussPost toDiscussPost(NewDiscussPostDTO dto, Long userId){
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

    public static SimpleDiscussPostVO toSimpleDiscussPostVO(DiscussPost dto){
        SimpleDiscussPostVO simpleDiscussPostVO = new SimpleDiscussPostVO();
        simpleDiscussPostVO.setId(dto.getId());
        simpleDiscussPostVO.setTitle(dto.getTitle());
        simpleDiscussPostVO.setContent(dto.getContent());
        simpleDiscussPostVO.setType(dto.getType());
        simpleDiscussPostVO.setStatus(dto.getStatus().getValue());
        simpleDiscussPostVO.setCreateTime(dto.getCreateTime());
        simpleDiscussPostVO.setScore(dto.getScore());
        simpleDiscussPostVO.setBoardId(dto.getBoardId());
        return simpleDiscussPostVO;
    }
}
