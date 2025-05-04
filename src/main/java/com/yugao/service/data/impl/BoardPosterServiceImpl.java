package com.yugao.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yugao.domain.BoardPoster;
import com.yugao.mapper.BoardPosterMapper;
import com.yugao.service.data.BoardPosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Boolean deleteBoardPosterByUserId(Long userId) {
        LambdaUpdateWrapper<BoardPoster> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BoardPoster::getUserId, userId);
        return boardPosterMapper.delete(wrapper) > 0;
    }

    @Override
    public Boolean deleteBoardPosterByBoardId(Long boardId) {
        LambdaUpdateWrapper<BoardPoster> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BoardPoster::getBoardId, boardId);
        return boardPosterMapper.delete(wrapper) > 0;
    }
}
