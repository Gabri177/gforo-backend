package com.yugao.controller.post;

import com.yugao.dto.CommentToCommentDTO;
import com.yugao.dto.CommentToPostDTO;
import com.yugao.dto.CommonContentDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.CommentBusinessService;
import com.yugao.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentBusinessService commentBusinessService;

    @PostMapping("/topost")
    public ResponseEntity<ResultFormat> addCommentToPost(
            @Validated @RequestBody CommentToPostDTO commentToPostDTO){

        System.out.println("addCommentToPost: " + commentToPostDTO);
        return commentBusinessService.addCommentToPost(commentToPostDTO);
    }

    @PostMapping("/tocomment")
    public ResponseEntity<ResultFormat> addCommentToComment(
            @Validated @RequestBody CommentToCommentDTO commentToCommentDTO){

        System.out.println("addCommentToComment: " + commentToCommentDTO);
        return commentBusinessService.addCommentToComment(commentToCommentDTO);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResultFormat> deleteComment(@PathVariable Long commentId){
        System.out.println("deleteComment: " + commentId);
        return commentBusinessService.deleteComment(commentId);
    }

    @PutMapping("/update")
    public ResponseEntity<ResultFormat> updateComment(
            @Validated({ValidationGroups.Comment.class})
            @RequestBody CommonContentDTO commonContentDTO
            ) {
        System.out.println("updateComment: " + commonContentDTO);
        return commentBusinessService.updateComment(commonContentDTO);
    }
}
