package com.yugao.service.validator;

import com.yugao.domain.board.Board;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.exception.BusinessException;
import com.yugao.service.data.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardValidator {

    private final BoardService boardService;

    public void checkBoardIds(List<Long> ids) {
        List<Long> boardIds = boardService.getAllBoard(StatusEnum.NORMAL)
                .stream()
                .map(Board::getId)
                .toList();
        if (ids.stream().noneMatch(boardIds::contains))
            throw new BusinessException(ResultCodeEnum.BOARD_NOT_EXISTS);
    }
}
