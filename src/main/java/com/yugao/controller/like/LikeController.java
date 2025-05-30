package com.yugao.controller.like;

import com.yugao.result.ResultFormat;
import com.yugao.service.business.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;


    @PreAuthorize("hasAnyAuthority('like:post')")
    @PostMapping("/post/{postId}")
    public ResponseEntity<ResultFormat> likePost(
            @PathVariable("postId") Long postId) {

        return likeService.likePost(postId);
    }

    @PreAuthorize("hasAnyAuthority('like:comment')")
    @PostMapping("/comment/{commentId}")
    public ResponseEntity<ResultFormat> likeComment(
            @PathVariable("commentId") Long commentId) {

        return likeService.likeComment(commentId);
    }
}
