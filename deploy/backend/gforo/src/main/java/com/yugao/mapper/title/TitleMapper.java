package com.yugao.mapper.title;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.title.Title;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TitleMapper extends BaseMapper<Title> {

    @Select("""
    SELECT t.*
    FROM title t
    INNER JOIN user u ON u.title_id = t.id
    WHERE u.id = #{userId}
    """)
    Title selectTitleByUserId(@Param("userId") Long userId);

}
