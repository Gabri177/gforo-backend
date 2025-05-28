package com.yugao.mapper.board;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.board.Board;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper extends BaseMapper<Board> {
}
