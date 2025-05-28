package com.yugao.service.business.board.board;

import com.yugao.domain.board.Board;
import com.yugao.dto.board.AddBoardDTO;
import com.yugao.dto.board.UpdateBoardDTO;
import com.yugao.enums.StatusEnum;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.builder.VOBuilder;
import com.yugao.service.business.board.BoardBusinessService;
import com.yugao.service.data.board.BoardService;
import com.yugao.service.validator.BoardValidator;
import com.yugao.vo.board.BoardInfosItemVO;
import com.yugao.vo.board.BoardInfosVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardBusinessServiceImpl implements BoardBusinessService {

    private final BoardService boardService;
    private final VOBuilder VOBuilder;
    private final BoardValidator boardValidator;

    @Override
    public ResponseEntity<ResultFormat> getBoardInfoList() {
        BoardInfosVO boardInfosVO = new BoardInfosVO();
        List<BoardInfosItemVO> boardInfosItems = new ArrayList<>();
        List<Board> allBoard = boardService.getAllBoard(StatusEnum.NORMAL);
        boardInfosVO.setTotalCount((long) allBoard.size());
        for (Board board : allBoard) {

            BoardInfosItemVO boardInfosItemVO = VOBuilder.buildBoardInfosItemVO(board);
            boardInfosItems.add(boardInfosItemVO);
        }
        boardInfosVO.setBoardInfos(boardInfosItems);
        return ResultResponse.success(boardInfosVO);
    }

    @Override
    public ResponseEntity<ResultFormat> getAllBoardDetail() {

        return ResultResponse.success(
                boardService.getAllBoard(StatusEnum.NULL)
        );
    }

    @Override
    public ResponseEntity<ResultFormat> updateBoard(UpdateBoardDTO dto) {

        System.out.println("UpdateBoardDTO: " + dto);
        Board board = new Board();
//        board = boardService.getBoardById(dto.getId());
        BeanUtils.copyProperties(dto, board);
        System.out.println(board.getStatus().getValue());
        System.out.println("board: " + board);
        boardService.updateBoard(board);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> addBoard(AddBoardDTO dto) {

        Board board = new Board();
        BeanUtils.copyProperties(dto, board);
        board.setStatus(StatusEnum.NORMAL);
        board.setCreateTime(new Date());
        boardService.addBoard(board);
        return ResultResponse.success(null);
    }
}
