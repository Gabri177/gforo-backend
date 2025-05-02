package com.yugao.converter;

import com.yugao.domain.Comment;
import com.yugao.dto.comment.CommentToCommentDTO;
import com.yugao.dto.comment.CommentToPostDTO;
import com.yugao.enums.CommentEntityTypeEnum;
import com.yugao.enums.StatusEnum;

import java.util.Date;

public class CommentConverter {

    public static Comment toPostDTOtoComment(CommentToPostDTO dto, Long userId){
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setEntityType(CommentEntityTypeEnum.POST);
        comment.setEntityId(dto.getEntityId());
        comment.setTargetId(0L);
        comment.setContent(dto.getContent());
        comment.setStatus(StatusEnum.NORMAL);
        comment.setCreateTime(new Date());
        comment.setParentId(0L);
        return comment;
    }

    public static Comment toCommentDTOtoComment(CommentToCommentDTO dto, Long userId){
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setEntityType(dto.getEntityType());
        comment.setEntityId(dto.getEntityId());
        comment.setTargetId(dto.getTargetId());
        comment.setContent(dto.getContent());
        comment.setStatus(StatusEnum.NORMAL);
        comment.setCreateTime(new Date());
        comment.setParentId(0L);
        return comment;
    }
}
