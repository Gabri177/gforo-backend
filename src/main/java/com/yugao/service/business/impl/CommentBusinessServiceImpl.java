package com.yugao.service.business.impl;

import com.yugao.converter.CommentConverter;
import com.yugao.domain.Comment;
import com.yugao.dto.CommentToCommentDTO;
import com.yugao.dto.CommentToPostDTO;
import com.yugao.dto.CommonContentDTO;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.CommentBusinessService;
import com.yugao.service.data.CommentService;
import com.yugao.service.validator.CommentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommentBusinessServiceImpl implements CommentBusinessService {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentValidator commentValidator;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getPrincipal().toString());
    }

    @Override
    public ResponseEntity<ResultFormat> addCommentToPost(CommentToPostDTO commentToPostDTO) {

        Long currentUserId = getCurrentUserId();
        Comment newComment = CommentConverter.toPostDTOtoComment(commentToPostDTO, currentUserId);
        commentValidator.check(newComment);
        System.out.println("addCommentToPost: " + newComment);
        commentService.addComment(newComment);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> addCommentToComment(CommentToCommentDTO commentToCommentDTO) {

        Long currentUserId = getCurrentUserId();
        Comment newComment = CommentConverter.toCommentDTOtoComment(commentToCommentDTO, currentUserId);
        commentValidator.check(newComment);
        System.out.println("addCommentToComment: " + newComment);
        commentService.addComment(newComment);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> deleteComment(Long commentId) {

        Long currentUserId = getCurrentUserId();
        Comment comment = commentService.findCommentById(commentId);
        if (comment == null)
            throw new BusinessException(ResultCode.COMMENT_NOT_FOUND);
        if (!comment.getUserId().equals(currentUserId))
            throw new BusinessException(ResultCode.USER_NOT_AUTHORIZED);
        commentService.deleteComment(commentId);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> updateComment(CommonContentDTO commonContentDTO) {

        Long currentUserId = getCurrentUserId();
        Comment comment = commentService.findCommentById(commonContentDTO.getId());
        if (comment == null)
            throw new BusinessException(ResultCode.COMMENT_NOT_FOUND);
        if (!comment.getUserId().equals(currentUserId))
            throw new BusinessException(ResultCode.USER_NOT_AUTHORIZED);
        comment.setContent(commonContentDTO.getContent());
        commentService.updateComment(comment);
        return ResultResponse.success(null);
    }
}
