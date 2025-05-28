package com.yugao.service.data.statistic;

import com.yugao.domain.statistic.UserActivityStats;
import com.yugao.vo.statistics.MonthlyUserStatsVO;

import java.time.LocalDate;
import java.util.List;

public interface UserActivityStatsService {
    void recordDailyActive();
    void recordMonthlyActive();
    List<UserActivityStats> getRecentDailyStats(int days);
    Integer selectActiveCountByDate(LocalDate date);
    List<MonthlyUserStatsVO> getMonthlyActiveStats();

}