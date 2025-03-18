package com.yugao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.domain.DiscussPost;
import com.yugao.mapper.DiscussPostMapper;
import com.yugao.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;


    @Override
    public IPage<DiscussPost> getDiscussPosts(int userId, int current, int limit, int orderMode) {
        LambdaQueryWrapper<DiscussPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(DiscussPost::getStatus, 2);
        wrapper.eq(userId != 0, DiscussPost::getUserId, userId);
        wrapper.orderByDesc(orderMode == 0, DiscussPost::getType, DiscussPost::getCreateTime);
        wrapper.orderByDesc(orderMode == 1, DiscussPost::getType, DiscussPost::getScore, DiscussPost::getCreateTime);
        Page<DiscussPost> page = new Page<>(current, limit);
        return discussPostMapper.selectPage(page, wrapper);
    }


    @Override
    public int getDiscussPostRows(int userId) {
        LambdaQueryWrapper<DiscussPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(DiscussPost::getStatus, 2);
        if (userId != 0)
            wrapper.eq(DiscussPost::getUserId, userId);
        return Math.toIntExact(discussPostMapper.selectCount(wrapper));
    }

    @Override
    public int addDiscussPost(DiscussPost discussPost) {
        return discussPostMapper.insert(discussPost);
    }

    @Override
    public DiscussPost getDiscussPostById(int id) {
        return discussPostMapper.selectById(id);
    }

    @Override
    public int updateCommentCount(int id, int commentCount) {
        LambdaUpdateWrapper<DiscussPost> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DiscussPost::getId, id).set(DiscussPost::getCommentCount, commentCount);
        return discussPostMapper.update(null, wrapper);
    }

    @Override
    public int updateType(int id, int type) {
        LambdaUpdateWrapper<DiscussPost> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DiscussPost::getId, id).set(DiscussPost::getType, type);
        return discussPostMapper.update(null, wrapper);
    }

    @Override
    public int updateStatus(int id, int status) {
        LambdaUpdateWrapper<DiscussPost> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DiscussPost::getId, id).set(DiscussPost::getStatus, status);
        return discussPostMapper.update(null, wrapper);
    }

    @Override
    public int updateScore(int id, double score) {
        LambdaUpdateWrapper<DiscussPost> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DiscussPost::getId, id).set(DiscussPost::getScore, score);
        return discussPostMapper.update(null, wrapper);
    }
}
