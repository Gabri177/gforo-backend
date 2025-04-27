package com.yugao.controller.post;

import com.yugao.dto.CommentToCommentDTO;
import com.yugao.dto.CommentToPostDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.CommentBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentBusinessService commentBusinessService;

    @PostMapping("/topost")
    public ResponseEntity<ResultFormat> addCommentToPost(@RequestBody CommentToPostDTO commentToPostDTO){

        System.out.println("addCommentToPost: " + commentToPostDTO);
        return commentBusinessService.addCommentToPost(commentToPostDTO);
    }

    @PostMapping("/tocomment")
    public ResponseEntity<ResultFormat> addCommentToComment(@RequestBody CommentToCommentDTO commentToCommentDTO){

        System.out.println("addCommentToComment: " + commentToCommentDTO);
        return commentBusinessService.addCommentToComment(commentToCommentDTO);
    }
}
