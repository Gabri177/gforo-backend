package com.yugao.service.business.post;

import com.yugao.dto.comment.CommentToCommentDTO;
import com.yugao.dto.comment.CommentToPostDTO;
import com.yugao.dto.comment.CommonContentDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface CommentBusinessService {

    public ResponseEntity<ResultFormat> addCommentToPost(CommentToPostDTO commentToPostDTO);

    public ResponseEntity<ResultFormat> addCommentToComment(CommentToCommentDTO commentToCommentDTO);

    public ResponseEntity<ResultFormat> deleteComment(Long commentId);

    public ResponseEntity<ResultFormat> updateComment(CommonContentDTO commonContentDTO);


}
