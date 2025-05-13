package com.yugao.service.business.post;

import com.yugao.dto.comment.CommentToCommentDTO;
import com.yugao.dto.comment.CommentToPostDTO;
import com.yugao.dto.comment.CommonContentDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface CommentBusinessService {

    ResponseEntity<ResultFormat> addCommentToPost(CommentToPostDTO commentToPostDTO);

    ResponseEntity<ResultFormat> addCommentToComment(CommentToCommentDTO commentToCommentDTO);

    ResponseEntity<ResultFormat> deleteComment(Long commentId);

    ResponseEntity<ResultFormat> updateComment(CommonContentDTO commonContentDTO);

    ResponseEntity<ResultFormat> getCommentLocation(Long commentId);

}
