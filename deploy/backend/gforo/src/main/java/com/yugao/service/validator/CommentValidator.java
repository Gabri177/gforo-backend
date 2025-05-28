package com.yugao.service.validator;

import com.yugao.domain.comment.Comment;
import com.yugao.domain.post.DiscussPost;
import com.yugao.domain.user.User;
import com.yugao.enums.CommentEntityTypeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.service.data.comment.CommentService;
import com.yugao.service.data.post.DiscussPostService;
import com.yugao.service.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentValidator {

    private final DiscussPostService discussPostService;
    private final CommentService commentService;
    private final UserHandler userHandler;

    public Comment checkId(Long commentId){

        Comment comment = commentService.findCommentById(commentId);
        if (comment == null)
            throw new BusinessException(ResultCodeEnum.COMMENT_NOT_FOUND);
        return comment;
    }

    public void check(Comment comment) {

        Long entityId = comment.getEntityId();
        Long targetId = comment.getTargetId();
        CommentEntityTypeEnum entityType = comment.getEntityType();

        if (entityType == CommentEntityTypeEnum.POST || entityType == CommentEntityTypeEnum.POST_FLOOR) {
            DiscussPost curPost = discussPostService.getDiscussPostById(entityId);
            if (curPost == null)
                throw new BusinessException(ResultCodeEnum.POST_NOT_FOUND);
        } else if (entityType == CommentEntityTypeEnum.POST_COMMENT_FLOOR) {
//            System.out.println("entityId: " + entityId);
            Comment curComment = commentService.findCommentById(entityId);
            if (curComment == null){
                throw new BusinessException(ResultCodeEnum.COMMENT_ENTITY_NOT_FOUND);
            }
            if (targetId != 0){
                // 1111111111111111111
                User targetUser = userHandler.getUser(targetId);
                if (targetUser == null)
                    throw new BusinessException(ResultCodeEnum.COMMENT_TARGET_NOT_FOUND);
            }
        } else {
            throw new BusinessException(ResultCodeEnum.COMMENT_TYPE_UNKNOWN);
        }
    }
}
