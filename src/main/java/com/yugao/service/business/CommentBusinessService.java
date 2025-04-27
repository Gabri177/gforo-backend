package com.yugao.service.business;

import com.yugao.dto.CommentToCommentDTO;
import com.yugao.dto.CommentToPostDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface CommentBusinessService {

    public ResponseEntity<ResultFormat> addCommentToPost(CommentToPostDTO commentToPostDTO);

    public ResponseEntity<ResultFormat> addCommentToComment(CommentToCommentDTO commentToCommentDTO);


}
