package com.yugao.service.data;

import com.yugao.domain.comment.Comment;
import java.util.List;
import java.util.Map;

public interface CommentService {

    Long getCommentCountByUserId(Long userId);

    Long getCommentCountByPostId(Long postId);

    Long getCommentCountByPostIds(List<Long> postIds);

    List<Comment> getCommentListByUserId(Long userId, Integer currentPage, Integer pageSize, Boolean isAsc);

    List<Comment> getCommentListByPostId(Long postId, Long currentPage, Integer pageSize, Boolean isAsc);

    List<Comment> getCommentListByPostId(Long postId);

    List<Comment> findCommentsToPostFloor(Long postId);

    List<Comment> findCommentsToPost(Long postId, Long current, Integer limit, Boolean isAsc);

    List<Comment> findCommentListOfComment(Long EntityId);

    List<Comment> findCommentListByPostIds(List<Long> postIds,
                                           Long current,
                                           Integer limit,
                                           Boolean isAsc);

    Boolean addComment(Comment comment);

    Boolean deleteComment(Long commentId);

    List<Comment> findCommentListOfUser(Long userId, Long current, Integer limit);

    Comment findCommentById(Long commentId);

    Boolean updateContent(Long id, String content);

    Boolean updateComment(Comment comment);

    Integer getTodayCommentCount();

    Double getMonthGrowthRate();

    Map<Long, Comment> getCommentMapCacheByUserId(Long userId);

}

