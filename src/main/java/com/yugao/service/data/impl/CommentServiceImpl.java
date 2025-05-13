package com.yugao.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.domain.comment.Comment;
import com.yugao.enums.CommentEntityTypeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.mapper.post.CommentMapper;
import com.yugao.service.data.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Long getCommentCountByUserId(Long userId) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, StatusEnum.DELETED);
        queryWrapper.eq(Comment::getUserId, userId);
        if (userId != 0L) {
            queryWrapper.eq(Comment::getUserId, userId);
        }
        return commentMapper.selectCount(queryWrapper);
    }

    @Override
    public Long getCommentCountByPostId(Long postId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, StatusEnum.DELETED);
        queryWrapper.eq(Comment::getEntityType, CommentEntityTypeEnum.POST);
        if (postId != 0L)
            queryWrapper.eq(Comment::getEntityId, postId);
        return commentMapper.selectCount(queryWrapper);
    }

    @Override
    public Long getCommentCountByPostIds(List<Long> postIds) {

        if (postIds == null || postIds.isEmpty())
            return 0L;

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, StatusEnum.DELETED);
        queryWrapper.in(Comment::getEntityType, CommentEntityTypeEnum.POST);
        queryWrapper.in(Comment::getEntityId, postIds);
        return commentMapper.selectCount(queryWrapper);
    }

    @Override
    public List<Comment> getCommentListByUserId(Long userId, Integer currentPage, Integer pageSize, Boolean isAsc) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, StatusEnum.DELETED);
        queryWrapper.eq(Comment::getUserId, userId);
        if (isAsc) {
            queryWrapper.orderByAsc(Comment::getCreateTime);
        } else {
            queryWrapper.orderByDesc(Comment::getCreateTime);
        }
        Page<Comment> page = new Page<>(currentPage, pageSize);
        return commentMapper.selectPage(page, queryWrapper).getRecords();
    }

    @Override
    public List<Comment> getCommentListByPostId(Long postId, Long currentPage, Integer pageSize, Boolean isAsc) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, StatusEnum.DELETED);
        if (postId != 0L)
            queryWrapper.eq(Comment::getEntityId, postId);
        if (isAsc) {
            queryWrapper.orderByAsc(Comment::getCreateTime);
        } else {
            queryWrapper.orderByDesc(Comment::getCreateTime);
        }
        Page<Comment> page = new Page<>(currentPage, pageSize);
        return commentMapper.selectPage(page, queryWrapper).getRecords();
    }

    @Override
    public List<Comment> getCommentListByPostId(Long postId) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, StatusEnum.DELETED);
        if (postId != 0L)
            queryWrapper.eq(Comment::getEntityId, postId);
        return commentMapper.selectList(queryWrapper);
    }

    /**
     * 查询评论状态为正常的 针对帖子楼层的评论信息
     * @param postId
     * @return
     */
    @Override
    public List<Comment> findCommentsToPostFloor(Long postId) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, StatusEnum.DELETED);
        queryWrapper.eq(Comment::getEntityType, CommentEntityTypeEnum.POST_FLOOR);
        queryWrapper.eq(Comment::getEntityId, postId);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        return commentMapper.selectList(queryWrapper);
    }

    /**
     * 查询评论状态为正常的 针对帖子的评论信息
     * @param postId
     * @return
     */
    @Override
    public List<Comment> findCommentsToPost(Long postId, Long current, Integer limit, Boolean isAsc) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, StatusEnum.DELETED);
        queryWrapper.eq(Comment::getEntityType, CommentEntityTypeEnum.POST);
        queryWrapper.eq(Comment::getEntityId, postId);
        if (isAsc) {
            queryWrapper.orderByAsc(Comment::getCreateTime);
        } else {
            queryWrapper.orderByDesc(Comment::getCreateTime);
        }
        Page<Comment> page = new Page<>(current, limit);
        return commentMapper.selectPage(page, queryWrapper).getRecords();
    }

    /**
     * 查询评论状态为正常的 针对评论的评论信息
     * 需要注意的是 这里的EntityId不再是帖子ID 而是评论ID
     * @param EntityId
     * @return
     */
    @Override
    public List<Comment> findCommentListOfComment(Long EntityId) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, StatusEnum.DELETED);
        queryWrapper.eq(Comment::getEntityType, CommentEntityTypeEnum.POST_COMMENT_FLOOR);
        queryWrapper.eq(Comment::getEntityId, EntityId);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        return commentMapper.selectList(queryWrapper);
    }

    @Override
    public List<Comment> findCommentListByPostIds(List<Long> postIds, Long current, Integer limit, Boolean isAsc) {

        if (postIds == null || postIds.isEmpty())
            return null;

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, StatusEnum.DELETED);
        queryWrapper.in(Comment::getEntityType, CommentEntityTypeEnum.POST);
        queryWrapper.in(Comment::getEntityId, postIds);
        if (isAsc) {
            queryWrapper.orderByAsc(Comment::getCreateTime);
        } else {
            queryWrapper.orderByDesc(Comment::getCreateTime);
        }
        Page<Comment> page = new Page<>(current, limit);
        return commentMapper.selectPage(page, queryWrapper).getRecords();
    }

    @Override
    public Boolean addComment(Comment comment) {
        return commentMapper.insert(comment) > 0;
    }

    @Override
    public Boolean deleteComment(Long commentId) {
        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Comment::getId, commentId);
        updateWrapper.set(Comment::getStatus, StatusEnum.DELETED);
        return commentMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public List<Comment> findCommentListOfUser(Long userId, Long current, Integer limit) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, StatusEnum.DELETED);
        queryWrapper.eq(Comment::getUserId, userId);
        queryWrapper.orderByAsc(Comment::getCreateTime);

        Page<Comment> page = new Page<>(current, limit);
        return commentMapper.selectPage(page, queryWrapper).getRecords();
    }

    @Override
    public Comment findCommentById(Long commentId) {

        return commentMapper.selectById(commentId);
    }

    @Override
    public Boolean updateContent(Long id, String content) {

        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Comment::getId, id);
        updateWrapper.set(Comment::getContent, content);
        return commentMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public Boolean updateComment(Comment comment) {

        return commentMapper.updateById(comment) > 0;
    }

    @Override
    public Integer getTodayCommentCount() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0);

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Comment::getCreateTime, startOfDay);
        queryWrapper.le(Comment::getCreateTime, now);
        return commentMapper.selectCount(queryWrapper).intValue();
    }

    @Override
    public Double getMonthGrowthRate() {

        LocalDateTime firstDayOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime firstDayOfLastMonth = firstDayOfMonth.minusMonths(1);

        Long currentMonthCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .ge(Comment::getCreateTime, firstDayOfMonth)
                .lt(Comment::getCreateTime, LocalDateTime.now()));
        Long lastMonthCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .ge(Comment::getCreateTime, firstDayOfLastMonth)
                .lt(Comment::getCreateTime, firstDayOfMonth));

        double growthRate = 0.0;
        if (lastMonthCount != 0)
            growthRate = ((double) (currentMonthCount - lastMonthCount) / lastMonthCount) * 100;

        return growthRate;
    }

    public Map<Long, Comment> getCommentMapCacheByUserId(Long userId) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, StatusEnum.DELETED);
        queryWrapper.eq(Comment::getUserId, userId);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        Map<Long, Comment> commentMapCache = new HashMap<>();
        for (Comment comment : comments) {
            commentMapCache.put(comment.getId(), comment);
        }
        return commentMapCache;
    }

}
