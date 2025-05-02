package com.yugao.service.validator;

import com.yugao.domain.Comment;
import com.yugao.domain.DiscussPost;
import com.yugao.domain.User;
import com.yugao.enums.CommentEntityTypeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.service.data.CommentService;
import com.yugao.service.data.DiscussPostService;
import com.yugao.service.data.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentValidator {

    @Autowired
    UserService userService;

    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    public void check(Comment comment) {

        Long entityId = comment.getEntityId();
        Long targetId = comment.getTargetId();
        CommentEntityTypeEnum entityType = comment.getEntityType();

        if (entityType == CommentEntityTypeEnum.POST || entityType == CommentEntityTypeEnum.POST_FLOOR) {
            DiscussPost curPost = discussPostService.getDiscussPostById(entityId);
            if (curPost == null)
                throw new BusinessException(ResultCode.POST_NOT_FOUND);
        } else if (entityType == CommentEntityTypeEnum.POST_COMMENT_FLOOR) {
            System.out.println("entityId: " + entityId);
            Comment curComment = commentService.findCommentById(entityId);
            if (curComment == null){
                throw new BusinessException(ResultCode.COMMENT_ENTITY_NOT_FOUND);
            }
            if (targetId != 0){
                User targetUser = userService.getUserById(targetId);
                if (targetUser == null)
                    throw new BusinessException(ResultCode.COMMENT_TARGET_NOT_FOUND);
            }
        } else {
            throw new BusinessException(ResultCode.COMMENT_TYPE_UNKNOWN);
        }
    }
}
