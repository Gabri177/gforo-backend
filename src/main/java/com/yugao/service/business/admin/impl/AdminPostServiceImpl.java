package com.yugao.service.business.admin.impl;

import com.yugao.domain.post.DiscussPost;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.security.LoginUser;
import com.yugao.service.business.admin.AdminPostService;
import com.yugao.service.data.permission.BoardPosterService;
import com.yugao.service.data.post.DiscussPostService;
import com.yugao.service.handler.EventHandler;
import com.yugao.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPostServiceImpl implements AdminPostService {

    private final DiscussPostService discussPostService;
    private final BoardPosterService boardPosterService;
    private final EventHandler eventHandler;

    @Override
    public ResponseEntity<ResultFormat> deletePost(Long postId) {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null)
            throw new BusinessException(ResultCodeEnum.USER_NOT_AUTHORIZED);
        DiscussPost post = discussPostService.getDiscussPostById(postId);
        if (!loginUser.hasAuthority("post:delete:any")) {
            List<Long> userBoardIds = boardPosterService.getBoardIdsByUserId(loginUser.getId());
            System.out.println("delete PostID = " + postId);
            System.out.println("userBoardIds = " + userBoardIds);
            if (!userBoardIds.contains(post.getBoardId()))
                throw new BusinessException(ResultCodeEnum.USER_NOT_AUTHORIZED);
        }
        discussPostService.deleteDiscussPost(postId);
        eventHandler.notifyDelete(post.getUserId(), DiscussPost.class, post);
        return ResultResponse.success(null);
    }
}
