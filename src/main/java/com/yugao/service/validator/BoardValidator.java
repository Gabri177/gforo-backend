package com.yugao.service.validator;

import com.yugao.domain.board.Board;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.service.data.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardValidator {

    @Autowired
    private BoardService boardService;

    public void checkBoardIds(List<Long> ids) {
        List<Long> boardIds = boardService.getAllBoard()
                .stream()
                .map(Board::getId)
                .toList();
        if (ids.stream().noneMatch(boardIds::contains))
            throw new BusinessException(ResultCodeEnum.BOARD_NOT_EXISTS);
    }
}
