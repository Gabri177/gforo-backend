package com.yugao.service.data.board.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yugao.domain.board.Board;
import com.yugao.enums.StatusEnum;
import com.yugao.mapper.board.BoardMapper;
import com.yugao.service.data.board.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;


    @Override
    public Long getBoardCount() {
        LambdaQueryWrapper<Board> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Board::getCreateTime);
        return boardMapper.selectCount(queryWrapper);
    }

    @Override
    public Boolean addBoard(String boardName, String boardDesc) {
        Board board = new Board();
        board.setName(boardName);
        board.setDescription(boardDesc);
        board.setStatus(StatusEnum.NORMAL);
        return boardMapper.insert(board) > 0;
    }

    @Override
    public void addBoard(Board board) {

        boardMapper.insert(board);
    }

    @Override
    public Boolean updateBoard(Long boardId, String boardName, String boardDesc) {
        LambdaUpdateWrapper<Board> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Board::getId, boardId);
        updateWrapper.set(Board::getName, boardName);
        updateWrapper.set(Board::getDescription, boardDesc);
        return boardMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public void updateBoard(Board board) {

        int count = boardMapper.updateById(board);
        System.out.println("Update count: " + count);
    }

    @Override
    public Boolean deleteBoard(Long boardId) {
        LambdaUpdateWrapper<Board> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Board::getId, boardId);
        updateWrapper.set(Board::getStatus, StatusEnum.DELETED);
        return boardMapper.update(null, updateWrapper) > 0;
    }


    @Override
    public Boolean isExistBoard(Long boardId) {
        LambdaQueryWrapper<Board> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Board::getId, boardId);
        queryWrapper.eq(Board::getStatus, StatusEnum.NORMAL);
        return boardMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public List<Board> getAllBoard(StatusEnum status) {
        LambdaQueryWrapper<Board> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(status != StatusEnum.NULL, Board::getStatus, status);
        queryWrapper.orderByDesc(Board::getCreateTime);
        return boardMapper.selectList(queryWrapper);
    }

    @Override
    public Board getBoardById(Long boardId) {

        LambdaQueryWrapper<Board> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Board::getId, boardId);
        return boardMapper.selectOne(queryWrapper);
    }


}
