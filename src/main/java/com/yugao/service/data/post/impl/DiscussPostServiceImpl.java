package com.yugao.service.data.post.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.domain.post.DiscussPost;
import com.yugao.enums.StatusEnum;
import com.yugao.mapper.post.DiscussPostMapper;
import com.yugao.service.data.post.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Service
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Override
    public List<DiscussPost> getDiscussPosts(Long userId, Long boardId, int current, int limit, int orderMode) {
        LambdaQueryWrapper<DiscussPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(DiscussPost::getStatus, StatusEnum.DELETED);
        wrapper.eq(userId != 0, DiscussPost::getUserId, userId);
        wrapper.eq(boardId != 0, DiscussPost::getBoardId, boardId);
        wrapper.orderByDesc(orderMode == 0, DiscussPost::getCreateTime); // DiscussPost::getType,
        wrapper.orderByDesc(orderMode == 1, DiscussPost::getType, DiscussPost::getScore, DiscussPost::getCreateTime);
        Page<DiscussPost> page = new Page<>(current, limit);
        return discussPostMapper.selectPage(page, wrapper).getRecords();
    }

    @Override
    public List<DiscussPost> getDiscussPostsByIds(List<Long> ids) {

        if(ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<DiscussPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(DiscussPost::getStatus, StatusEnum.DELETED);
        wrapper.in(DiscussPost::getId, ids);
        return discussPostMapper.selectList(wrapper);
    }


    @Override
    public Long getDiscussPostRows(Long userId, Long boardId) {
        LambdaQueryWrapper<DiscussPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(DiscussPost::getStatus, StatusEnum.DELETED);
        if (userId != 0)
            wrapper.eq(DiscussPost::getUserId, userId);
        if (boardId != 0)
            wrapper.eq(DiscussPost::getBoardId, boardId);
        return discussPostMapper.selectCount(wrapper);
    }

    @Override
    public List<Long> getDiscussPostIdsByBoardId(Long boardId) {
        LambdaQueryWrapper<DiscussPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(DiscussPost::getStatus, StatusEnum.DELETED);
        wrapper.eq(DiscussPost::getBoardId, boardId);
        wrapper.select(DiscussPost::getId);
        return discussPostMapper.selectList(wrapper).stream()
                .map(DiscussPost::getId)
                .toList();
    }

    @Override
    public DiscussPost getLatestDiscussPostByBoardId(Long boardId) {
        LambdaQueryWrapper<DiscussPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(DiscussPost::getStatus, StatusEnum.DELETED);
        wrapper.eq(DiscussPost::getBoardId, boardId);
        wrapper.orderByDesc(DiscussPost::getCreateTime);
        wrapper.last("LIMIT 1");
        return discussPostMapper.selectOne(wrapper);
    }


    @Override
    public int addDiscussPost(DiscussPost discussPost) {

        return discussPostMapper.insert(discussPost);
    }

    @Override
    public DiscussPost getDiscussPostById(Long id) {
        LambdaUpdateWrapper<DiscussPost> wrapper = new LambdaUpdateWrapper<>();
        wrapper.ne(DiscussPost::getStatus, StatusEnum.DELETED);
        wrapper.eq(DiscussPost::getId, id);
        return discussPostMapper.selectOne(wrapper);
    }

    @Override
    public int updateType(Long id, int type) {
        LambdaUpdateWrapper<DiscussPost> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DiscussPost::getId, id).set(DiscussPost::getType, type);
        return discussPostMapper.update(null, wrapper);
    }

    @Override
    public int updateStatus(Long id, StatusEnum status) {
        LambdaUpdateWrapper<DiscussPost> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DiscussPost::getId, id).set(DiscussPost::getStatus, status);
        return discussPostMapper.update(null, wrapper);
    }

    @Override
    public int updateScore(Long id, double score) {
        LambdaUpdateWrapper<DiscussPost> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DiscussPost::getId, id).set(DiscussPost::getScore, score);
        return discussPostMapper.update(null, wrapper);
    }

    @Override
    public Boolean deleteDiscussPost(Long id) {
        LambdaUpdateWrapper<DiscussPost> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DiscussPost::getId, id)
                .set(DiscussPost::getStatus, StatusEnum.DELETED);
        return discussPostMapper.update(null, wrapper) > 0;
    }

    @Override
    public Boolean updateDiscussPost(DiscussPost discussPost) {
        return discussPostMapper.updateById(discussPost) > 0;
    }

    @Override
    public Long getDiscussPostCount() {

        return discussPostMapper.selectCount(null);
    }

    @Override
    public Integer getTodayDiscussPostCount() {

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        return Math.toIntExact(discussPostMapper.selectCount(
                new LambdaQueryWrapper<DiscussPost>()
                        .between(DiscussPost::getCreateTime, todayStart, now)
//                        .ne(DiscussPost::getStatus, StatusEnum.DELETED)
        ));
    }

    @Override
    public Double getMonthGrowthRate() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfThisMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime firstDayOfLastMonth = firstDayOfThisMonth.minusMonths(1);

        Long thisMonthCount = discussPostMapper.selectCount(
                new LambdaQueryWrapper<DiscussPost>()
                        .between(DiscussPost::getCreateTime,
                        firstDayOfThisMonth, now)
//                        .ne(DiscussPost::getStatus, StatusEnum.DELETED)
        );

        Long lastMonthCount = discussPostMapper.selectCount(
                new LambdaQueryWrapper<DiscussPost>()
                        .between(DiscussPost::getCreateTime,
                        firstDayOfLastMonth, firstDayOfThisMonth)
//                        .ne(DiscussPost::getStatus, StatusEnum.DELETED)
        );

        double growthRate = 0.0;
        if (lastMonthCount != 0) {
            growthRate = (double) (thisMonthCount - lastMonthCount) / lastMonthCount * 100;
        }
        return Math.round(growthRate * 100.0) / 100.0;
    }

    @Override
    public Integer selectCountByCreateTime(LocalDateTime startTime, LocalDateTime endTime) {

        return discussPostMapper.selectCountByCreateTime(startTime, endTime);
    }

}
