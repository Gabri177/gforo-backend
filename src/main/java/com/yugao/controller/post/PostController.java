package com.yugao.controller.post;

import com.yugao.dto.comment.CommonContentDTO;
import com.yugao.dto.post.NewDiscussPostDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.post.PostService;
import com.yugao.validation.ValidationGroups;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/detail/{postId}")
    public ResponseEntity<ResultFormat> getPostDetail(
            @PathVariable Long postId,
            @RequestParam (defaultValue = "1", name = "currentPage", required = false) Long currentPage,
            @RequestParam (defaultValue = "10", name = "pageSize", required = false) Integer pageSize,
            @RequestParam (defaultValue = "true", name = "isAsc", required = false) Boolean isAsc
            ) {
//        System.out.println("getPostDetail: " + postId + " " + currentPage + " " + pageSize + " " + isAsc);
        return postService.getPostDetail(postId, currentPage, pageSize, isAsc);
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<ResultFormat> getPostsInBoard(
            @PathVariable("boardId") Long boardId,
            @RequestParam(defaultValue = "1", name = "currentPage", required = false) Integer currentPage,
            @RequestParam(defaultValue = "10", name = "pageSize", required = false) Integer pageSize,
            @RequestParam(defaultValue = "0", name = "orderMode", required = false) Integer orderMode
            ) {
//        System.out.println("getPostsInBoard: " + boardId + " " + currentPage + " " + pageSize + " " + orderMode);
        return postService.getPosts(0L, boardId, currentPage, pageSize, orderMode);
    }

    @PreAuthorize("hasAnyAuthority('post:publish')")
    @PostMapping("/publish")
    public ResponseEntity<ResultFormat> publishPost(
            @Validated @RequestBody NewDiscussPostDTO newDiscussPostDTO) {
        System.out.println("publishPost: " + newDiscussPostDTO);
        return postService.publishPost(newDiscussPostDTO);
    }

    @PreAuthorize("hasAnyAuthority('post:delete:own')")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ResultFormat> deletePost(@PathVariable Long postId) {
        System.out.println("deletePost: " + postId);
        return postService.deletePost(postId);
    }

    @PreAuthorize("hasAnyAuthority('post:update:own')")
    @PutMapping("/update")
    public ResponseEntity<ResultFormat> updatePost(
            @Validated({ValidationGroups.Post.class})
            @RequestBody CommonContentDTO commonContentDTO
            ){
        System.out.println("updatePost: " + commonContentDTO);
        return postService.updatePost(commonContentDTO);
    }

    @PreAuthorize("hasAnyAuthority('post:change-type:board', 'post:change-type:any')")
    @PutMapping("/change-type/{postId}/{type}")
    public ResponseEntity<ResultFormat> changePostType(
            @PathVariable Long postId,
            @PathVariable Integer type
            ){
        System.out.println("changePostType: " + postId + " " + type);
        return postService.changePostType(postId, type);
    }
}
