package com.yugao.controller.board;

import com.yugao.dto.board.AddBoardDTO;
import com.yugao.dto.board.UpdateBoardDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.board.BoardBusinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardBusinessService boardBusinessService;

    @GetMapping
    public ResponseEntity<ResultFormat> getBoardList(){
        return boardBusinessService.getBoardInfoList();
    }

    @PreAuthorize("hasAnyAuthority('board:detail:any')")
    @GetMapping("/detail")
    public ResponseEntity<ResultFormat> getAllBoardDetail() {

        return boardBusinessService.getAllBoardDetail();
    }

    @PreAuthorize("hasAnyAuthority('board:update:any')")
    @PutMapping("/update")
    public ResponseEntity<ResultFormat> updateBoard(
            @Validated @RequestBody UpdateBoardDTO dto
            ) {

        return boardBusinessService.updateBoard(dto);
    }

    @PreAuthorize("hasAnyAuthority('board:add:any')")
    @PostMapping("/add")
    public ResponseEntity<ResultFormat> addBoard(
            @Validated @RequestBody AddBoardDTO dto
            ) {
        return boardBusinessService.addBoard(dto);
    }

}
