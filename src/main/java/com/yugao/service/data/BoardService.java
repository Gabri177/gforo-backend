package com.yugao.service.data;

import com.yugao.domain.board.Board;

import java.util.List;

public interface BoardService {

    Long getBoardCount();

    Boolean addBoard(String boardName, String boardDesc);

    Boolean updateBoard(Long boardId, String boardName, String boardDesc);

    Boolean deleteBoard(Long boardId);

    Boolean isExistBoard(Long boardId);

    List<Board> getAllBoard();
}
