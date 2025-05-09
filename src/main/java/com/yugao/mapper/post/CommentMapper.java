package com.yugao.mapper.post;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.post.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
