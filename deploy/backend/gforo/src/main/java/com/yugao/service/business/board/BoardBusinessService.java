package com.yugao.service.business.board;

import com.yugao.dto.board.AddBoardDTO;
import com.yugao.dto.board.UpdateBoardDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface BoardBusinessService {

    ResponseEntity<ResultFormat> getBoardInfoList();

    ResponseEntity<ResultFormat> getAllBoardDetail();

    ResponseEntity<ResultFormat> updateBoard(UpdateBoardDTO dto);

    ResponseEntity<ResultFormat> addBoard(AddBoardDTO dto);
}
