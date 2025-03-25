package com.yugao.service.data;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yugao.domain.DiscussPost;


public interface DiscussPostService {

    IPage<DiscussPost> getDiscussPosts(Long userId, int current, int limit, int orderMode);

    Long getDiscussPostRows(Long userId);

    int addDiscussPost(DiscussPost discussPost);

    DiscussPost getDiscussPostById(Long id);

    int updateCommentCount(Long id, int commentCount);

    int updateType(Long id, int type);

    int updateStatus(Long id, int status);

    int updateScore(Long id, double score);
}
