package com.yugao.service.business.like;

import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface LikeService {

    ResponseEntity<ResultFormat> likePost(Long postId);

    ResponseEntity<ResultFormat> likeComment(Long commentId);

    Integer countLikePost(Long postId);

    Integer countLikeComment(Long commentId);

    Integer countUserGetLikes(Long userId);

    Boolean checkLikePost(Long postId);

    Boolean checkLikeComment(Long commentId);
}
