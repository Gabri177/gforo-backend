package com.yugao.service.data;

import com.yugao.domain.Comment;

import java.util.List;

public interface CommentService {

    public Long getCommentCount(Long userId);

    public List<Comment> findCommentsToPostFloor(Long postId);

    public List<Comment> findCommentsToPost(Long postId, Long current, Integer limit);

    public List<Comment> findCommentListOfComment(Long EntityId);
}
