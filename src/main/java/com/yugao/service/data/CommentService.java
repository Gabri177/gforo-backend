package com.yugao.service.data;

import com.yugao.domain.Comment;

import java.util.List;

public interface CommentService {

    Long getCommentCount(Long userId);

    List<Comment> findCommentsToPostFloor(Long postId);

    List<Comment> findCommentsToPost(Long postId, Long current, Integer limit);

    List<Comment> findCommentListOfComment(Long EntityId);

    Boolean addComment(Comment comment);

    Boolean deleteComment(Long commentId);

    List<Comment> findCommentListOfUser(Long userId, Long current, Integer limit);

    Comment findCommentById(Long commentId);
}
