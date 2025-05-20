package com.yugao.mapper.comment;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.comment.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT COUNT(*) FROM comment " +
            "WHERE create_time >= #{start} AND create_time < #{end}")
    Integer selectCountByCreateTime(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);
}
