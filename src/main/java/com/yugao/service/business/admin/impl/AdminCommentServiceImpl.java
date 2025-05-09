package com.yugao.service.business.admin.impl;

import com.yugao.domain.post.Comment;
import com.yugao.domain.post.DiscussPost;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.security.LoginUser;
import com.yugao.service.business.admin.AdminCommentService;
import com.yugao.service.data.BoardPosterService;
import com.yugao.service.data.CommentService;
import com.yugao.service.data.DiscussPostService;
import com.yugao.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminCommentServiceImpl implements AdminCommentService {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private BoardPosterService boardPosterService;

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
        if (loginuser == null)
            throw new BusinessException(ResultCodeEnum.USER_NOT_LOGIN);
        if (loginuser.hasAuthority("comment:delete:board") && !loginuser.hasAuthority("comment:delete:any")){
            Comment comment = commentService.findCommentById(commentId);
            DiscussPost post = discussPostService.getDiscussPostById(comment.getPostId());
            List<Long> userBoardIds = boardPosterService.getBoardIdsByUserId(loginuser.getId());
            if (!userBoardIds.contains(post.getBoardId()))
                throw new BusinessException(ResultCodeEnum.NO_PERMISSION);
        }
        commentService.deleteComment(commentId);
        return ResultResponse.success(null);
    }
}
