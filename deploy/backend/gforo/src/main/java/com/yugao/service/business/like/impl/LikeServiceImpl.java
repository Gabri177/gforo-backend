package com.yugao.service.business.like.impl;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.comment.Comment;
import com.yugao.domain.post.DiscussPost;
import com.yugao.enums.*;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.base.RedisService;
import com.yugao.service.business.like.LikeService;
import com.yugao.service.data.comment.CommentService;
import com.yugao.service.data.post.DiscussPostService;
import com.yugao.service.business.title.TitleBusinessService;
import com.yugao.service.handler.EventHandler;
import com.yugao.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final RedisService redisService;
    private final DiscussPostService discussPostService;
    private final CommentService commentService;
    private final EventHandler eventHandler;
    private final TitleBusinessService titleBusinessService;

    private void incrPostLike(Long postId){
        String key = RedisKeyConstants.buildLikeKeyWithTargetId(postId, LikeTypeEnum.POST);
        redisService.increment(key, 1);
    }

    private void decrPostLike(Long postId){
        String key = RedisKeyConstants.buildLikeKeyWithTargetId(postId, LikeTypeEnum.POST);
        redisService.increment(key, -1);
    }

    private void incrCommentLike(Long commentId){
        String key = RedisKeyConstants.buildLikeKeyWithTargetId(commentId, LikeTypeEnum.COMMENT);
        redisService.increment(key, 1);
    }

    private void decrCommentLike(Long commentId){
        String key = RedisKeyConstants.buildLikeKeyWithTargetId(commentId, LikeTypeEnum.COMMENT);
        redisService.increment(key, -1);
    }

    private void incrUserLike(Long userId){
        String key = RedisKeyConstants.buildLikeKeyWithTargetId(userId, LikeTypeEnum.USER);
        redisService.increment(key, 1);
    }

    private void decrUserLike(Long userId){
        String key = RedisKeyConstants.buildLikeKeyWithTargetId(userId, LikeTypeEnum.USER);
        redisService.increment(key, -1);
    }

    @Override
    public ResponseEntity<ResultFormat> likePost(Long postId) {
        Long userId = SecurityUtils.mustGetLoginUserId();
        String key = RedisKeyConstants.buildLikeKeyWithUserId(userId, LikeTypeEnum.POST, postId);


        DiscussPost post = discussPostService.getDiscussPostById(postId);
        if (post == null)
            throw new BusinessException(ResultCodeEnum.POST_NOT_FOUND);
        Long targetUserId = post.getUserId();

        BooleanEnum val;
        if (!redisService.hasKey(key))
            val = BooleanEnum.FALSE;
        else
            val = BooleanEnum.getBooleanEnum(redisService.get(key));

        if (val == BooleanEnum.TRUE) {
            redisService.delete(key);
            decrPostLike(postId);
            decrUserLike(targetUserId);

            // TODO: 可能要优化
            titleBusinessService.subtractExp(targetUserId, 1, "被取消点赞帖子", EntityTypeEnum.POST, postId);

        } else {
            redisService.set(key, BooleanEnum.TRUE.getValue());
            incrPostLike(postId);
            incrUserLike(targetUserId);

            // TODO: 可能要优化
            titleBusinessService.addExp(targetUserId, 1, "被点赞帖子", EntityTypeEnum.POST, postId);
            // 通知用户
            eventHandler.notifyLike(targetUserId, postId, true);
        }

        return ResultResponse.success(null);

    }

    @Override
    public ResponseEntity<ResultFormat> likeComment(Long commentId) {
        Long userId = SecurityUtils.mustGetLoginUserId();
        String key = RedisKeyConstants.buildLikeKeyWithUserId(userId, LikeTypeEnum.COMMENT, commentId);

        Comment comment = commentService.findCommentById(commentId);
        if (comment == null)
            throw new BusinessException(ResultCodeEnum.COMMENT_NOT_FOUND);
        Long targetUserId = comment.getUserId();

        BooleanEnum val;
        if (!redisService.hasKey(key))
            val = BooleanEnum.FALSE;
        else
            val = BooleanEnum.getBooleanEnum(redisService.get(key));
        if (val == BooleanEnum.TRUE) {
            redisService.delete(key);
            decrCommentLike(commentId);
            decrUserLike(targetUserId);

            titleBusinessService.subtractExp(targetUserId, 1, "被取消点赞评论", EntityTypeEnum.COMMENT, commentId);
        } else {
            redisService.set(key, BooleanEnum.TRUE.getValue());
            incrCommentLike(commentId);
            incrUserLike(targetUserId);

            titleBusinessService.addExp(targetUserId, 1, "被点赞评论", EntityTypeEnum.COMMENT, commentId);
            // 通知用户
            eventHandler.notifyLike(targetUserId, commentId, false);
        }
        return ResultResponse.success(null);
    }


    @Override
    public Integer countLikePost(Long postId) {
        String key = RedisKeyConstants.buildLikeKeyWithTargetId(postId, LikeTypeEnum.POST);
        if (!redisService.hasKey(key))
            return 0;
        return Integer.parseInt(redisService.get(key));
    }

    @Override
    public Integer countLikeComment(Long commentId) {
        String key = RedisKeyConstants.buildLikeKeyWithTargetId(commentId, LikeTypeEnum.COMMENT);
        if (!redisService.hasKey(key))
            return 0;
        return Integer.parseInt(redisService.get(key));
    }

    @Override
    public Integer countUserGetLikes(Long userId) {

        String key = RedisKeyConstants.buildLikeKeyWithTargetId(userId, LikeTypeEnum.USER);
        if (!redisService.hasKey(key))
            return 0;
        return Integer.parseInt(redisService.get(key));
    }

    @Override
    public Boolean checkLikePost(Long postId) {
        /**
         * 注意 因为我们post是白名单 所以在filter中检查了是否携带cookie 如果没有 则这里一定全是null
         */
        Long userId = SecurityUtils.getLoginUserId();
        if (postId == null)
            return false;
        String key = RedisKeyConstants.buildLikeKeyWithUserId(userId, LikeTypeEnum.POST, postId);
        if (!redisService.hasKey(key))
            return false;
        BooleanEnum val = BooleanEnum.getBooleanEnum(redisService.get(key));
        return val == BooleanEnum.TRUE;
    }

    @Override
    public Boolean checkLikeComment(Long commentId) {
        /**
         * 注意 因为我们post是白名单 所以在filter中检查了是否携带cookie 如果没有 则这里一定全是null
         */
        Long userId = SecurityUtils.getLoginUserId();
        if (commentId == null)
            return false;
        String key = RedisKeyConstants.buildLikeKeyWithUserId(userId, LikeTypeEnum.COMMENT, commentId);
        if (!redisService.hasKey(key))
            return false;
        BooleanEnum val = BooleanEnum.getBooleanEnum(redisService.get(key));
        return val == BooleanEnum.TRUE;
    }
}
