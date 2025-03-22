package com.yugao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yugao.domain.DiscussPost;


public interface DiscussPostService {

    IPage<DiscussPost> getDiscussPosts(int userId, int current, int limit, int orderMode);

    int getDiscussPostRows(int userId);

    int addDiscussPost(DiscussPost discussPost);

    DiscussPost getDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);

    int updateType(int id, int type);

    int updateStatus(int id, int status);

    int updateScore(int id, double score);
}
