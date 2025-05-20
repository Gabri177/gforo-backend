package com.yugao.service.data.permission.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yugao.domain.permission.BoardPoster;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.mapper.permission.BoardPosterMapper;
import com.yugao.service.data.permission.BoardPosterService;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BoardPosterServiceImpl implements BoardPosterService {

    @Autowired
    private BoardPosterMapper boardPosterMapper;

    @Override
    public Boolean addBoardPoster(BoardPoster boardPoster) {
        return boardPosterMapper.insert(boardPoster) > 0;
    }

    @Override
    public Boolean changeBoardPoster(BoardPoster boardPoster) {

        LambdaUpdateWrapper<BoardPoster> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BoardPoster::getUserId, boardPoster.getUserId());
        updateWrapper.set(BoardPoster::getBoardId, boardPoster.getBoardId());
        return boardPosterMapper.update(boardPoster, updateWrapper) > 0;
    }

    @Override
    public List<Long> getBoardIdsByUserId(Long userId) {

        LambdaQueryWrapper<BoardPoster> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BoardPoster::getUserId, userId);
        List<BoardPoster> boardPosters = boardPosterMapper.selectList(queryWrapper);
        return boardPosters.stream()
                .map(BoardPoster::getBoardId)
                .toList();
    }

    @Override
    public Long getUserIdByBoardId(Long boardId) {
        LambdaQueryWrapper<BoardPoster> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BoardPoster::getBoardId, boardId);
        BoardPoster boardPoster = boardPosterMapper.selectOne(queryWrapper);
        if (boardPoster != null) {
            return boardPoster.getUserId();
        }
        return null;
    }

    @Override
    public Boolean deleteBoardPoster(Long userId, Long boardId) {

        LambdaQueryWrapper<BoardPoster> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BoardPoster::getUserId, userId);
        queryWrapper.eq(BoardPoster::getBoardId, boardId);
        return boardPosterMapper.delete(queryWrapper) > 0;
    }

    @Override
    public Boolean deleteBoardPoster(BoardPoster boardPoster) {
        return deleteBoardPoster(boardPoster.getUserId(), boardPoster.getBoardId());
    }

    @Override
    public void deleteBoardPosterByUserId(Long userId) {

        LambdaQueryWrapper<BoardPoster> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BoardPoster::getUserId, userId);
        boardPosterMapper.delete(queryWrapper);
    }

    @Override
    public void addBoardPosters(List<BoardPoster> boardPosters) {

        List<BatchResult> results = boardPosterMapper.insert(boardPosters);
        long total = results.stream()
                .flatMapToInt(res -> Arrays.stream(res.getUpdateCounts()))
                .filter(i -> i == 1)
                .count();
        if (total != boardPosters.size())
            throw new BusinessException(ResultCodeEnum.SQL_UPDATING_ERROR);
    }
}
