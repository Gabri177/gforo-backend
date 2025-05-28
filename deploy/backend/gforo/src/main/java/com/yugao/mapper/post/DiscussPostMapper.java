package com.yugao.mapper.post;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.post.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface DiscussPostMapper extends BaseMapper<DiscussPost> {

    @Select("SELECT COUNT(*) FROM discuss_post " +
            "WHERE create_time >= #{start} AND create_time < #{end}")
    Integer selectCountByCreateTime(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);
}
