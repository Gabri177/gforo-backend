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

    // 删除帖子
    @PreAuthorize("hasAnyAuthority('post:delete:board', 'post:delete:any')")
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ResultFormat> deletePost(
            @PathVariable("postId") Long postId
    ){
        return adminPostService.deletePost(postId);
    }
}
