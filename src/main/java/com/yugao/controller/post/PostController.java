package com.yugao.controller.post;

import com.yugao.result.ResultFormat;
import com.yugao.service.business.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discuss")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{postId}/{currentPage}")
    public ResponseEntity<ResultFormat> getPostDetail(@PathVariable Long postId,
                                                      @PathVariable Long currentPage) {
        return postService.getPostDetail(postId, currentPage);
    }
}
