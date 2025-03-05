package com.yugao.service;

import com.yugao.domain.DiscussPost;

import java.util.List;

public interface DiscussPostService {

    public List<DiscussPost> getDiscussPosts(int userId, int offset, int limit, int orderMode);
    public int getDiscussPostRows(int userId);
    public int addDiscussPost(DiscussPost discussPost);
    public DiscussPost getDiscussPostById(int id);
    public int updateCommentCount(int id, int commentCount);
    public int updateType(int id, int type);
    public int updateStatus(int id, int status);
    public int updateScore(int id, double score);
}
