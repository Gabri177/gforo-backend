package com.yugao.controller.post;

import com.yugao.dto.comment.CommentToCommentDTO;
import com.yugao.dto.comment.CommentToPostDTO;
import com.yugao.dto.comment.CommonContentDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.post.CommentBusinessService;
import com.yugao.service.business.user.UserBusinessService;
import com.yugao.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentBusinessService commentBusinessService;
    @Autowired
    private UserBusinessService userBusinessService;

    @PreAuthorize("hasAnyAuthority('comment:publish:topost')")
    @PostMapping("/topost")
    public ResponseEntity<ResultFormat> addCommentToPost(
            @Validated @RequestBody CommentToPostDTO commentToPostDTO){

        System.out.println("addCommentToPost: " + commentToPostDTO);
        return commentBusinessService.addCommentToPost(commentToPostDTO);
    }

    @PreAuthorize("hasAnyAuthority('comment:publish:tocomment')")
    @PostMapping("/tocomment")
    public ResponseEntity<ResultFormat> addCommentToComment(
            @Validated @RequestBody CommentToCommentDTO commentToCommentDTO){

        System.out.println("addCommentToComment: " + commentToCommentDTO);
        return commentBusinessService.addCommentToComment(commentToCommentDTO);
    }

    // ------------
    // 这里可以考虑当一个帖子删除以后 同步标记这个帖子相关的所有评论都为删除状态
    // ------------
    @PreAuthorize("hasAnyAuthority('comment:delete:own')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResultFormat> deleteComment(@PathVariable Long commentId){
        System.out.println("deleteComment: " + commentId);
        return commentBusinessService.deleteComment(commentId);
    }

    @PreAuthorize("hasAnyAuthority('comment:update:own')")
    @PutMapping("/update")
    public ResponseEntity<ResultFormat> updateComment(
            @Validated({ValidationGroups.Comment.class})
            @RequestBody CommonContentDTO commonContentDTO
            ) {
        System.out.println("updateComment: " + commonContentDTO);
        return commentBusinessService.updateComment(commonContentDTO);
    }

    @GetMapping("/location/{commentId}")
    public ResponseEntity<ResultFormat> getComentLocation(
            @PathVariable(name = "commentId") Long commentId
    ) {
        return commentBusinessService.getCommentLocation(commentId);
    }
}
