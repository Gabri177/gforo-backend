package com.yugao.service.data;

import com.yugao.domain.post.DiscussPost;
import com.yugao.enums.StatusEnum;

import java.util.List;


public interface DiscussPostService {

    List<DiscussPost> getDiscussPosts(Long userId, Long boardId, int current, int limit, int orderMode);

    List<DiscussPost> getDiscussPostsByIds(List<Long> ids);

    Long getDiscussPostRows(Long userId, Long boardId);

    List<Long> getDiscussPostIdsByBoardId(Long boardId);

    DiscussPost getLatestDiscussPostByBoardId(Long boardId);

    int addDiscussPost(DiscussPost discussPost);

    DiscussPost getDiscussPostById(Long id);

    int updateType(Long id, int type);

    int updateStatus(Long id, StatusEnum status);

    int updateScore(Long id, double score);

    Boolean deleteDiscussPost(Long id);

    Boolean updateDiscussPost(DiscussPost discussPost);

    Long getDiscussPostCount();

    Integer getTodayDiscussPostCount();

    Double getMonthGrowthRate();


}
