package com.yugao.converter;

import com.yugao.domain.DiscussPost;
import com.yugao.vo.comment.CommentVO;
import com.yugao.vo.post.PostDetailVO;
import com.yugao.vo.user.SimpleUserVO;

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
