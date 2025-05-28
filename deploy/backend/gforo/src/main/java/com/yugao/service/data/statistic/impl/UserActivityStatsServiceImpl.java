package com.yugao.service.data.statistic.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.statistic.UserActivityStats;
import com.yugao.mapper.statistic.UserActivityStatsMapper;
import com.yugao.service.base.RedisService;
import com.yugao.service.data.statistic.UserActivityStatsService;
import com.yugao.vo.statistics.MonthlyUserStatsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityStatsServiceImpl implements UserActivityStatsService {

    private final RedisService redisService;
    private final UserActivityStatsMapper statsMapper;

    @Override
    @Scheduled(cron = "0 59 23 * * ?")
    public void recordDailyActive() {
        long count = redisService.sCard(RedisKeyConstants.DAILY_ACTIVE_USER);

        UserActivityStats stats = new UserActivityStats();
        stats.setStatDate(LocalDate.now());
        stats.setType("DAY");
        stats.setActiveUserCount((int) count);
        stats.setCreatedTime(LocalDateTime.now());

        statsMapper.insert(stats);
        redisService.delete(RedisKeyConstants.DAILY_ACTIVE_USER);
    }

    @Override
    @Scheduled(cron = "0 59 23 L * ?")
    public void recordMonthlyActive() {
        long count = redisService.sCard(RedisKeyConstants.MONTHLY_ACTIVE_USER);

        UserActivityStats stats = new UserActivityStats();
        stats.setStatMonth(YearMonth.now().toString());
        stats.setType("MONTH");
        stats.setActiveUserCount((int) count);
        stats.setCreatedTime(LocalDateTime.now());

        statsMapper.insert(stats);
        redisService.delete(RedisKeyConstants.MONTHLY_ACTIVE_USER);
    }

    @Override
    public List<UserActivityStats> getRecentDailyStats(int days) {
        LocalDate startDate = LocalDate.now().minusDays(days - 1);
        return statsMapper.selectList(new LambdaQueryWrapper<UserActivityStats>()
                .eq(UserActivityStats::getType, "DAY")
                .ge(UserActivityStats::getStatDate, startDate)
                .orderByAsc(UserActivityStats::getStatDate));
    }

    @Override
    public Integer selectActiveCountByDate(LocalDate date) {

        return statsMapper.selectActiveCountByDate(date);
    }

    @Override
    public List<MonthlyUserStatsVO> getMonthlyActiveStats() {

        return statsMapper.getMonthlyActiveStats();
    }

}
