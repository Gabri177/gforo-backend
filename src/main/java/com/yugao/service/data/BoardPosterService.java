package com.yugao.service.data;

import com.yugao.domain.BoardPoster;

import java.util.List;

// 可能要重新设计 考虑到一个用户可以为多个板块的版主
public interface BoardPosterService {

    Boolean addBoardPoster(BoardPoster boardPoster);
    Boolean changeBoardPoster(BoardPoster boardPoster);
    List<Long> getBoardIdsByUserId(Long userId);
    Long getUserIdByBoardId(Long boardId);
    Boolean deleteBoardPosterByUserId(Long userId);
    Boolean deleteBoardPosterByBoardId(Long boardId);
}
