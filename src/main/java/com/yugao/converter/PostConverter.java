package com.yugao.converter;

import com.yugao.domain.Comment;
import com.yugao.domain.DiscussPost;
import com.yugao.domain.User;
import com.yugao.vo.CommentVO;
import com.yugao.vo.PostDetailVO;
import com.yugao.vo.SimpleUserVO;

import java.util.List;

public class PostConverter {

    public static PostDetailVO toPostDetailVO(DiscussPost discussPost,
                                              SimpleUserVO auther,
                                              List<CommentVO> replies,
                                              Boolean isExpanded) {

        PostDetailVO vo = new PostDetailVO();
        vo.setId(discussPost.getId());
        vo.setTitle(discussPost.getTitle());
        vo.setContent(discussPost.getContent());
        vo.setCreateTime(discussPost.getCreateTime());
        vo.setIsExpanded(isExpanded);
        vo.setAuthor(auther);
        vo.setReplies(replies);
        return vo;

    }
}
