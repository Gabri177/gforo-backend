package com.yugao.converter;

import com.yugao.domain.comment.Comment;
import com.yugao.dto.comment.CommentToCommentDTO;
import com.yugao.dto.comment.CommentToPostDTO;
import com.yugao.enums.CommentEntityTypeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.vo.comment.CommentVO;
import com.yugao.vo.user.SimpleUserVO;

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
        comment.setPostId(dto.getEntityId());
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
        comment.setPostId(dto.getPostId());
        return comment;
    }

    public static CommentVO toCommentVO(Comment comment, SimpleUserVO targetUserInfo, SimpleUserVO author) {
        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setContent(comment.getContent());
        vo.setCreateTime(comment.getCreateTime());
        vo.setPostId(comment.getPostId());
        vo.setTargetUserInfo(targetUserInfo);
        vo.setAuthor(author);
        return vo;
    }
}
