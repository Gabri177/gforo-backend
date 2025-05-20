package com.yugao.service.business.admin.impl;

import com.yugao.domain.comment.Comment;
import com.yugao.domain.post.DiscussPost;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.security.LoginUser;
import com.yugao.service.business.admin.AdminCommentService;
import com.yugao.service.data.permission.BoardPosterService;
import com.yugao.service.data.comment.CommentService;
import com.yugao.service.data.post.DiscussPostService;
import com.yugao.service.handler.EventHandler;
import com.yugao.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCommentServiceImpl implements AdminCommentService {

    private final CommentService commentService;
    private final DiscussPostService discussPostService;
    private final BoardPosterService boardPosterService;
    private final EventHandler eventHandler;

    @Override
    public ResponseEntity<ResultFormat> getCommentList(Long boardId, Long currentPage, Integer pageSize, Boolean isAsc) {

        LoginUser loginuser = SecurityUtils.getLoginUser();
        if (loginuser == null)
            throw new BusinessException(ResultCodeEnum.USER_NOT_LOGIN);
        if (boardId == 0L && loginuser.hasAuthority("comment:info:board"))
            throw new BusinessException(ResultCodeEnum.NO_PERMISSION);
        List<Long> postIdsInBoard = discussPostService.getDiscussPostIdsByBoardId(boardId);
        List<Comment> comments = commentService.findCommentListByPostIds(postIdsInBoard, currentPage, pageSize, isAsc);
        return ResultResponse.success(comments);
    }

    @Override
    public ResponseEntity<ResultFormat> deleteComment(Long commentId) {

        LoginUser loginuser = SecurityUtils.getLoginUser();
        Comment comment = commentService.findCommentById(commentId);
        if (comment == null)
            throw new BusinessException(ResultCodeEnum.COMMENT_NOT_FOUND);
        if (loginuser == null)
            throw new BusinessException(ResultCodeEnum.USER_NOT_LOGIN);
        if (loginuser.hasAuthority("comment:delete:board") && !loginuser.hasAuthority("comment:delete:any")){
            DiscussPost post = discussPostService.getDiscussPostById(comment.getPostId());
            List<Long> userBoardIds = boardPosterService.getBoardIdsByUserId(loginuser.getId());
            if (!userBoardIds.contains(post.getBoardId()))
                throw new BusinessException(ResultCodeEnum.NO_PERMISSION);
        }
        commentService.deleteComment(commentId);
        eventHandler.notifyDelete(comment.getUserId(), Comment.class, comment);
        return ResultResponse.success(null);
    }
}
