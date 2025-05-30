package com.yugao.controller.admin;

import com.yugao.result.ResultFormat;
import com.yugao.service.business.admin.AdminCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/comment")
@RequiredArgsConstructor
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    // TODO:这个可能没有意义
    // 当boardId为0时，表示查询所有的评论  默认查询所有针对帖子的评论
    @PreAuthorize("hasAnyAuthority('comment:info:any', 'comment:info:board')")
    @GetMapping("/info/{boardId}")
    public ResponseEntity<ResultFormat> getCommentList(
            @PathVariable("boardId") Long boardId,
            @RequestParam(defaultValue = "1", name = "currentPage", required = false) Long currentPage,
            @RequestParam(defaultValue = "10", name = "pageSize", required = false) Integer pageSize,
            @RequestParam(defaultValue = "true", name = "isAsc", required = false) Boolean isAsc
    ){
        return adminCommentService.getCommentList(boardId, currentPage, pageSize, isAsc);
    }

    // 删除评论
    @PreAuthorize("hasAnyAuthority('comment:delete:any', 'comment:delete:board')")
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<ResultFormat> deleteComment(
            @PathVariable("commentId") Long commentId
    ){
        System.out.println("deleteComment: " + commentId);
        return adminCommentService.deleteComment(commentId);
    }
}
