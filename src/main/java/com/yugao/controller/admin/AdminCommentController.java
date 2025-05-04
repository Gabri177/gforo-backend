package com.yugao.controller.admin;

import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/comment")
public class AdminCommentController {

    // 当boardId为0时，表示查询所有的评论
    @GetMapping("/info/{boardId}")
    public ResponseEntity<ResultFormat> getCommentList(
            @PathVariable("boardId") Long boardId
    ){
        return null;
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResultFormat> deleteComment(
            @PathVariable("commentId") Long commentId
    ){
        return null;
    }
}
