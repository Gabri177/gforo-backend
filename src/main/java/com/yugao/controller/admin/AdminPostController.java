package com.yugao.controller.admin;

import com.yugao.result.ResultFormat;
import com.yugao.service.business.admin.AdminPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/post")
public class AdminPostController {

    @Autowired
    private AdminPostService adminPostService;

    // 当boardId为0时，表示查询所有的帖子
//    @PreAuthorize("hasAnyAuthority('post:info:any', 'post:info:board')")
//    @GetMapping("/info/{boardId}")
//    public ResponseEntity<ResultFormat> getPostList(
//            @PathVariable("boardId") Long boardId,
//            @RequestParam(defaultValue = "1", name = "currentPage", required = false) Long currentPage,
//            @RequestParam(defaultValue = "10", name = "pageSize", required = false) Integer pageSize,
//            @RequestParam(defaultValue = "0", name = "isAsc", required = false) Boolean isAsc
//    ){
//        return adminPostService.getPostList(boardId, currentPage, pageSize, isAsc);
//    }

    // 删除帖子
    @PreAuthorize("hasAnyAuthority('post:delete:board', 'post:delete:any')")
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ResultFormat> deletePost(
            @PathVariable("postId") Long postId
    ){
        return adminPostService.deletePost(postId);
    }
}
