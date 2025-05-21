package com.yugao.service.business.comment.impl;

import com.yugao.converter.CommentConverter;
import com.yugao.domain.comment.Comment;
import com.yugao.dto.comment.CommentToCommentDTO;
import com.yugao.dto.comment.CommentToPostDTO;
import com.yugao.dto.comment.CommonContentDTO;
import com.yugao.enums.*;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.comment.CommentBusinessService;
import com.yugao.service.data.comment.CommentService;
import com.yugao.service.business.title.TitleBusinessService;
import com.yugao.service.handler.EventHandler;
import com.yugao.service.validator.CommentValidator;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.comment.CommentLocationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentBusinessServiceImpl implements CommentBusinessService {

    private final CommentService commentService;
    private final CommentValidator commentValidator;
    private final EventHandler eventHandler;
    private final TitleBusinessService titleBusinessService;

    @Override
    public ResponseEntity<ResultFormat> addCommentToPost(CommentToPostDTO commentToPostDTO) {

        Long currentUserId = SecurityUtils.mustGetLoginUserId();
        Comment newComment = CommentConverter.toPostDTOtoComment(commentToPostDTO, currentUserId);
        commentValidator.check(newComment);
//        System.out.println("addCommentToPost: " + newComment);
        commentService.addComment(newComment);

        // TODO: 可能要优化
        titleBusinessService.addExp(currentUserId, 1, "评论帖子", EntityTypeEnum.COMMENT, newComment.getId());

        eventHandler.notifyComment(commentToPostDTO.getToPostUserId(), newComment);

        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> addCommentToComment(CommentToCommentDTO commentToCommentDTO) {

        Long currentUserId = SecurityUtils.mustGetLoginUserId();
        Comment newComment = CommentConverter.toCommentDTOtoComment(commentToCommentDTO, currentUserId);
        commentValidator.check(newComment);
//        System.out.println("addCommentToComment: " + newComment);
        commentService.addComment(newComment);

        // TODO: 可能要优化
        titleBusinessService.addExp(currentUserId, 1, "评论评论", EntityTypeEnum.COMMENT, newComment.getId());

        eventHandler.notifyComment(commentToCommentDTO.getToCommentUserId(), newComment);

        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> deleteComment(Long commentId) {

        Long currentUserId = SecurityUtils.mustGetLoginUserId();
        Comment comment = commentService.findCommentById(commentId);
        if (comment == null)
            throw new BusinessException(ResultCodeEnum.COMMENT_NOT_FOUND);
        if (!comment.getUserId().equals(currentUserId))
            throw new BusinessException(ResultCodeEnum.USER_NOT_AUTHORIZED);
        commentService.deleteComment(commentId);

        // TODO: 可能要优化
        titleBusinessService.subtractExp(currentUserId, 1, "删除评论", EntityTypeEnum.COMMENT, commentId);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> updateComment(CommonContentDTO commonContentDTO) {

        Long currentUserId = SecurityUtils.mustGetLoginUserId();
        Comment comment = commentService.findCommentById(commonContentDTO.getId());
        if (comment == null)
            throw new BusinessException(ResultCodeEnum.COMMENT_NOT_FOUND);
        if (!comment.getUserId().equals(currentUserId))
            throw new BusinessException(ResultCodeEnum.USER_NOT_AUTHORIZED);
        comment.setContent(commonContentDTO.getContent());
        commentService.updateComment(comment);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> getCommentLocation(Long commentId) {
        /**
         * 这里我们默认了页面大小是10
         * 后面可以增加参数进行修改
         * 此外这里是基于新评论是数据库自增 所以当并发情况下可能存在问题
         * 最好按照时间进行比较排序
         * 这个后面修改 是counts 的 filter 的逻辑比较问题 应该用 Date 而不是 id
         */
        // TODO: 此外这里是基于新评论是数据库自增 所以当并发情况下可能存在问题 最好按照时间进行比较排序
        CommentLocationVO commentLocationVO = new CommentLocationVO();
        commentLocationVO.setIsPostFloor(false);
        Comment curComment = commentValidator.checkId(commentId);
        if (curComment.getEntityType() == CommentEntityTypeEnum.POST_COMMENT_FLOOR ||
                curComment.getEntityType() == CommentEntityTypeEnum.POST_FLOOR)
            commentLocationVO.setTargetId(commentId);
        else
            commentLocationVO.setTargetId(0L);
        if (curComment.getEntityType() == CommentEntityTypeEnum.POST_FLOOR){
            commentLocationVO.setPage(1L);
            commentLocationVO.setEntityId(curComment.getPostId());
            commentLocationVO.setIsPostFloor(true);
            return ResultResponse.success(commentLocationVO);
        }
        List<Comment> cts = commentService.getCommentListByPostId(curComment.getPostId());
        Map<Long, Comment> commentMap = cts.stream()
                .collect(Collectors.toMap(Comment::getId, Function.identity()));
        while (curComment.getEntityType() != CommentEntityTypeEnum.POST){
            if (commentMap.get(curComment.getEntityId()) == null){
                commentLocationVO.setPage(1L);
                commentLocationVO.setEntityId(curComment.getPostId());
                commentLocationVO.setIsPostFloor(true);
                return ResultResponse.success(commentLocationVO);
            }
            curComment = commentMap.get(curComment.getEntityId());

        }
        commentLocationVO.setEntityId(curComment.getId());
        long counts = cts.stream()
                        .filter(cmt -> {
                            return cmt.getEntityType() == CommentEntityTypeEnum.POST &&
                                    cmt.getId() < commentId;
                        })
                        .count() + 1;
        System.out.println("counts =============== " + counts);
        commentLocationVO.setPage((long) Math.ceil(counts / 10.0));
        return ResultResponse.success(commentLocationVO);
    }
}
