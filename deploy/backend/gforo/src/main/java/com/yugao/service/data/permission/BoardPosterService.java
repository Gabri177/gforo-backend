package com.yugao.service.data.permission;

import com.yugao.domain.permission.BoardPoster;

import java.util.List;

// 可能要重新设计 考虑到一个用户可以为多个板块的版主
public interface BoardPosterService {

    Boolean addBoardPoster(BoardPoster boardPoster);
    Boolean changeBoardPoster(BoardPoster boardPoster);
    List<Long> getBoardIdsByUserId(Long userId);
    Long getUserIdByBoardId(Long boardId);
    Boolean deleteBoardPoster(Long userId, Long boardId);
    Boolean deleteBoardPoster(BoardPoster boardPoster);
    void deleteBoardPosterByUserId(Long userId);
    void addBoardPosters(List<BoardPoster> boardPosters);
}
