package com.yugao.service.business.statistic.impl;

import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.statistic.StatisticsService;
import com.yugao.service.data.CommentService;
import com.yugao.service.data.DiscussPostService;
import com.yugao.service.data.UserService;
import com.yugao.vo.admin.DashboardStatsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserService userService;
    private final DiscussPostService discussPostService;
    private final CommentService commentService;

    @Override
    public ResponseEntity<ResultFormat> getDashboardStats() {

        DashboardStatsVO vo = new DashboardStatsVO();
        vo.setTotalUsers(userService.getUserCount());
        vo.setTotalPosts(discussPostService.getDiscussPostCount());
        vo.setTotalComments(commentService.getCommentCountByPostId(0L));
//        vo.setActiveUsersToday();
//        vo.setVisitToday();
        vo.setNewPostsToday(discussPostService.getTodayDiscussPostCount());
        vo.setNewCommentsToday(commentService.getTodayCommentCount());
        vo.setUserGrowthPercent(userService.getMonthGrowthRate());
        vo.setPostGrowthPercent(discussPostService.getMonthGrowthRate());
        vo.setCommentGrowthPercent(commentService.getMonthGrowthRate());
        return ResultResponse.success(vo);
    }
}
