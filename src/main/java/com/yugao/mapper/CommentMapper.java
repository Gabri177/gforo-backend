package com.yugao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
