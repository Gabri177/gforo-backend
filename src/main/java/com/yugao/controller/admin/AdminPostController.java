package com.yugao.controller.admin;

import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/post")
public class AdminPostController {

    // 当boardId为0时，表示查询所有的帖子
    @GetMapping("/info/{boardId}")
    public ResponseEntity<ResultFormat> getPostList(
            @PathVariable("boardId") Long boardId
    ){
        return null;
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ResultFormat> deletePost(
            @PathVariable("postId") Long postId
    ){
        return null;
    }
}
