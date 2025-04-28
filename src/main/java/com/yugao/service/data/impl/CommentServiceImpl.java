package com.yugao.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.domain.Comment;
import com.yugao.mapper.CommentMapper;
import com.yugao.service.data.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Long getCommentCount(Long postId) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, 1);
        queryWrapper.eq(Comment::getEntityType, 0);
        if (postId != 0L)
            queryWrapper.eq(Comment::getEntityId, postId);
        return commentMapper.selectCount(queryWrapper);
    }

    /**
     * 查询评论状态为正常的 针对帖子楼层的评论信息
     * @param postId
     * @return
     */
    @Override
    public List<Comment> findCommentsToPostFloor(Long postId) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, 1);
        queryWrapper.eq(Comment::getEntityType, 1);
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
    public List<Comment> findCommentsToPost(Long postId, Long current, Integer limit) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, 1);
        queryWrapper.eq(Comment::getEntityType, 0);
        queryWrapper.eq(Comment::getEntityId, postId);
        queryWrapper.orderByAsc(Comment::getCreateTime);

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
        queryWrapper.ne(Comment::getStatus, 1);
        queryWrapper.eq(Comment::getEntityType, 2);
        queryWrapper.eq(Comment::getEntityId, EntityId);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        return commentMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean addComment(Comment comment) {
        return commentMapper.insert(comment) > 0;
    }

    @Override
    public Boolean deleteComment(Long commentId) {
        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Comment::getId, commentId);
        updateWrapper.set(Comment::getStatus, 1); // 设置为删除状态
        return commentMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public List<Comment> findCommentListOfUser(Long userId, Long current, Integer limit) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Comment::getStatus, 1);
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


}
