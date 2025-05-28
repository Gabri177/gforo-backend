package com.yugao.service.data.board;

import com.yugao.domain.board.Board;
import com.yugao.enums.StatusEnum;

import java.util.List;

public interface BoardService {

    Long getBoardCount();

    Boolean addBoard(String boardName, String boardDesc);

    void addBoard(Board board);

    Boolean updateBoard(Long boardId, String boardName, String boardDesc);

    void updateBoard(Board board);

    Boolean deleteBoard(Long boardId);

    Boolean isExistBoard(Long boardId);

    List<Board> getAllBoard(StatusEnum status);

    Board getBoardById(Long boardId);
}
