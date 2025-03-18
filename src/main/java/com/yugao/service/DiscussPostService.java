package com.yugao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yugao.domain.DiscussPost;
import org.springframework.transaction.annotation.Transactional;


public interface DiscussPostService {

    IPage<DiscussPost> getDiscussPosts(int userId, int current, int limit, int orderMode);

    int getDiscussPostRows(int userId);

    @Transactional
    int addDiscussPost(DiscussPost discussPost);

    DiscussPost getDiscussPostById(int id);

    @Transactional
    int updateCommentCount(int id, int commentCount);
    @Transactional

    int updateType(int id, int type);
    @Transactional

    int updateStatus(int id, int status);
    @Transactional

    int updateScore(int id, double score);
}
