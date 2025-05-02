package com.yugao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.Board;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper extends BaseMapper<Board> {
}
