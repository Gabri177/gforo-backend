package com.yugao.converter;

import com.yugao.domain.post.DiscussPost;
import com.yugao.vo.comment.CommentVO;
import com.yugao.vo.post.PostDetailVO;
import com.yugao.vo.post.SimpleDiscussPostVO;
import com.yugao.vo.user.SimpleUserVO;

import java.util.List;

public class PostConverter {

    public static PostDetailVO toPostDetailVO(DiscussPost discussPost,
                                              SimpleUserVO auther,
                                              List<CommentVO> replies) {

        PostDetailVO vo = new PostDetailVO();
        vo.setId(discussPost.getId());
        vo.setTitle(discussPost.getTitle());
        vo.setContent(discussPost.getContent());
        vo.setCreateTime(discussPost.getCreateTime());
        vo.setAuthor(auther);
        vo.setReplies(replies);
        return vo;

    }

    public static SimpleDiscussPostVO toSimpleDiscussPostVO(DiscussPost discussPost) {
        SimpleDiscussPostVO vo = new SimpleDiscussPostVO();
        vo.setId(discussPost.getId());
        vo.setTitle(discussPost.getTitle());
        vo.setType(discussPost.getType());
        vo.setStatus(discussPost.getStatus().getValue());
        vo.setCreateTime(discussPost.getCreateTime());
        vo.setScore(discussPost.getScore());
        vo.setBoardId(discussPost.getBoardId());
        vo.setContent(discussPost.getContent());
        return vo;
    }
}
