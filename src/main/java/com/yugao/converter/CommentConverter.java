package com.yugao.converter;

import com.yugao.domain.Comment;
import com.yugao.dto.comment.CommentToCommentDTO;
import com.yugao.dto.comment.CommentToPostDTO;

import java.util.Date;

public class CommentConverter {

    public static Comment toPostDTOtoComment(CommentToPostDTO dto, Long userId){
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setEntityType(0);
        comment.setEntityId(dto.getEntityId());
        comment.setTargetId(0L);
        comment.setContent(dto.getContent());
        comment.setStatus(0);
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
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        comment.setParentId(0L);
        return comment;
    }
}
