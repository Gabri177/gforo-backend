package com.yugao.controller.post;

import com.yugao.dto.comment.CommonContentDTO;
import com.yugao.dto.post.NewDiscussPostDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.post.PostService;
import com.yugao.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{postId}/{currentPage}")
    public ResponseEntity<ResultFormat> getPostDetail(
            @PathVariable Long postId,
            @PathVariable Long currentPage) {
        System.out.println("getPostDetail: " + postId + " " + currentPage);
        return postService.getPostDetail(postId, currentPage);
    }

    @PostMapping("/publish")
    public ResponseEntity<ResultFormat> publishPost(
            @Validated @RequestBody NewDiscussPostDTO newDiscussPostDTO) {
        System.out.println("publishPost: " + newDiscussPostDTO);
        return postService.publishPost(newDiscussPostDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ResultFormat> deletePost(@PathVariable Long postId) {
        System.out.println("deletePost: " + postId);
        return postService.deletePost(postId);
    }

    @PutMapping("/update")
    public ResponseEntity<ResultFormat> updatePost(
            @Validated({ValidationGroups.Post.class})
            @RequestBody CommonContentDTO commonContentDTO
            ){
        System.out.println("updatePost: " + commonContentDTO);
        return postService.updatePost(commonContentDTO);
    }
}
