package com.yugao.converter;

import com.yugao.domain.comment.Comment;
import com.yugao.domain.post.DiscussPost;
import com.yugao.vo.comment.CommentVO;
import com.yugao.vo.post.PostDetailVO;
import com.yugao.vo.post.SimpleDiscussPostVO;
import com.yugao.vo.user.SimpleUserVO;

import java.util.List;

public class PostConverter {

    public static PostDetailVO toPostDetailVO(DiscussPost discussPost,
                                              SimpleUserVO auther,
                                              List<CommentVO> replies,
                                              Integer likeCount,
                                              Boolean isLike) {

        PostDetailVO vo = new PostDetailVO();
        vo.setId(discussPost.getId());
        vo.setTitle(discussPost.getTitle());
        vo.setContent(discussPost.getContent());
        vo.setCreateTime(discussPost.getCreateTime());
        vo.setAuthor(auther);
        vo.setReplies(replies);
        vo.setLikeCount(likeCount);
        vo.setIsLike(isLike);
        vo.setType(discussPost.getType());
        return vo;
    }

    public static PostDetailVO toPostDetailVO(Comment comment,
                                              SimpleUserVO auther,
                                              List<CommentVO> replies,
                                              Integer likeCount,
                                              Boolean isLike) {

        PostDetailVO vo = new PostDetailVO();
        vo.setId(comment.getId());
        vo.setTitle(null);
        vo.setContent(comment.getContent());
        vo.setCreateTime(comment.getCreateTime());
        vo.setAuthor(auther);
        vo.setReplies(replies);
        vo.setLikeCount(likeCount);
        vo.setIsLike(isLike);
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
