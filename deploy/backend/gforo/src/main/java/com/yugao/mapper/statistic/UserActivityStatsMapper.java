package com.yugao.mapper.statistic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.statistic.UserActivityStats;
import com.yugao.vo.statistics.MonthlyUserStatsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;


@Mapper
public interface UserActivityStatsMapper extends BaseMapper<UserActivityStats> {

    @Select("SELECT active_user_count FROM user_activity_stats " +
            "WHERE stat_date = #{date} AND type = 'DAY' LIMIT 1")
    Integer selectActiveCountByDate(@Param("date") LocalDate date);

    // 查询近12个月的活跃用户数量（按月份统计，来自 user_activity_stats 表）
    @Select("""
        SELECT
            stat_month AS month,
            active_user_count AS activeUsers
        FROM user_activity_stats
        WHERE type = 'MONTH'
          AND stat_month >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 11 MONTH), '%Y-%m')
        ORDER BY stat_month ASC
    """)
    List<MonthlyUserStatsVO> getMonthlyActiveStats();
}
