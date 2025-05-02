package com.yugao.service.business.board.board;

import com.yugao.domain.Board;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.builder.BoardInfosItemBuilder;
import com.yugao.service.business.board.BoardBusinessService;
import com.yugao.service.data.BoardService;
import com.yugao.vo.board.BoardInfosItemVO;
import com.yugao.vo.board.BoardInfosVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardBusinessServiceImpl implements BoardBusinessService {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardInfosItemBuilder boardInfosItemBuilder;

    @Override
    public ResponseEntity<ResultFormat> getBoardInfoList() {
        BoardInfosVO boardInfosVO = new BoardInfosVO();
        List<BoardInfosItemVO> boardInfosItems = new ArrayList<>();
        List<Board> allBoard = boardService.getAllBoard();
        boardInfosVO.setTotalCount((long) allBoard.size());
        for (Board board : allBoard) {

            BoardInfosItemVO boardInfosItemVO = boardInfosItemBuilder.buildBoardInfosItemVO(board);
            boardInfosItems.add(boardInfosItemVO);
        }
        boardInfosVO.setBoardInfos(boardInfosItems);
        return ResultResponse.success(boardInfosVO);
    }
}
