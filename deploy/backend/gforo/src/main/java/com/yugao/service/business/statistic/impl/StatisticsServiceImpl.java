package com.yugao.service.business.statistic.impl;

import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.statistic.StatisticsService;
import com.yugao.service.data.comment.CommentService;
import com.yugao.service.data.post.DiscussPostService;
import com.yugao.service.data.statistic.UserActivityStatsService;
import com.yugao.service.data.user.UserService;
import com.yugao.service.handler.OnlineUserHandler;
import com.yugao.service.handler.VisitStatisticsHandler;
import com.yugao.vo.statistics.DailyActivityVO;
import com.yugao.vo.statistics.DashboardStatsVO;
import com.yugao.vo.statistics.MonthlyUserStatsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserService userService;
    private final DiscussPostService discussPostService;
    private final CommentService commentService;
    private final OnlineUserHandler onlineUserHandler;
    private final VisitStatisticsHandler visitStatisticsHandler;
    private final UserActivityStatsService userActivityStatsService;

    @Override
    public ResponseEntity<ResultFormat> getDashboardStats() {

        DashboardStatsVO vo = new DashboardStatsVO();
        vo.setTotalUsers(userService.getUserCount());
        vo.setTotalPosts(discussPostService.getDiscussPostCount());
        vo.setTotalComments(commentService.getCommentCountByPostId(0L));
        vo.setActiveUsers(onlineUserHandler.getOnlineCount());
        vo.setVisitToday(visitStatisticsHandler.getTodayPv());
        vo.setNewPostsToday(discussPostService.getTodayDiscussPostCount());
        vo.setNewCommentsToday(commentService.getTodayCommentCount());
        vo.setUserGrowthPercent(userService.getMonthGrowthRate());
        vo.setPostGrowthPercent(discussPostService.getMonthGrowthRate());
        vo.setCommentGrowthPercent(commentService.getMonthGrowthRate());
        return ResultResponse.success(vo);
    }

    @Override
    public ResponseEntity<ResultFormat> getLast7DaysActivity() {
        List<DailyActivityVO> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();

            // 查询活跃用户数
            Integer activeUserCount = userActivityStatsService.selectActiveCountByDate(date);

            // 查询发帖数
            Integer postCount = discussPostService.selectCountByCreateTime(start, end);

            // 查询评论数
            Integer commentCount = commentService.selectCountByCreateTime(start, end);

            DailyActivityVO vo = new DailyActivityVO();
            vo.setDate(date);
            vo.setActiveUsers(activeUserCount != null ? activeUserCount : 0);
            vo.setPostCount(postCount != null ? postCount : 0);
            vo.setCommentCount(commentCount != null ? commentCount : 0);

            result.add(vo);
        }

        return ResultResponse.success(result);
    }

    @Override
    public ResponseEntity<ResultFormat> getMonthlyRegistrationAndActiveStats() {
        List<MonthlyUserStatsVO> registerList = userService.getMonthlyRegisterStats();
        List<MonthlyUserStatsVO> activeList = userActivityStatsService.getMonthlyActiveStats();

        // 构造近12个月的月份列表（格式：yyyy-MM）
        List<String> last12Months = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 11; i >= 0; i--) {
            last12Months.add(today.minusMonths(i).getYear() + "-" +
                    String.format("%02d", today.minusMonths(i).getMonthValue()));
        }

        // 将数据库查询结果转换为 Map
        Map<String, MonthlyUserStatsVO> registerMap = registerList.stream()
                .collect(Collectors.toMap(MonthlyUserStatsVO::getMonth, r -> r));

        Map<String, MonthlyUserStatsVO> activeMap = activeList.stream()
                .collect(Collectors.toMap(MonthlyUserStatsVO::getMonth, a -> a));

        // 构造完整的结果
        List<MonthlyUserStatsVO> result = new ArrayList<>();
        for (String month : last12Months) {
            MonthlyUserStatsVO vo = new MonthlyUserStatsVO();
            vo.setMonth(month);
            vo.setRegisteredUsers(registerMap.getOrDefault(month, new MonthlyUserStatsVO()).getRegisteredUsers() != null
                    ? registerMap.get(month).getRegisteredUsers()
                    : 0);
            vo.setActiveUsers(activeMap.getOrDefault(month, new MonthlyUserStatsVO()).getActiveUsers() != null
                    ? activeMap.get(month).getActiveUsers()
                    : 0);
            result.add(vo);
        }

        return ResultResponse.success(result);
    }

}
