package com.yugao.controller.board;

import com.yugao.result.ResultFormat;
import com.yugao.service.business.board.BoardBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/board")
public class BoardController {


    @Autowired
    private BoardBusinessService boardBusinessService;

    @GetMapping
    public ResponseEntity<ResultFormat> getBoardList(){
        return boardBusinessService.getBoardInfoList();
    }
}
