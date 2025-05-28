package com.yugao.service.handler;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.service.base.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class VisitStatisticsHandler {

    private final RedisService redisService;

    public void recordVisit(Long userId) {
        String pvKey = RedisKeyConstants.buildSystemPvKey(LocalDate.now());
        redisService.increment(RedisKeyConstants.SYSTEM_PV_TOTAL, 1);
        redisService.increment(pvKey, 1);
        redisService.expire(pvKey, 7, TimeUnit.DAYS);

        if (userId != null) {
            String uvKey = RedisKeyConstants.buildSystemUvKey(LocalDate.now());
            redisService.sAdd(uvKey, userId.toString());
            redisService.expire(uvKey, 7, TimeUnit.DAYS);
        }
    }

    public Long getTotalPv() {
        String value = redisService.get(RedisKeyConstants.SYSTEM_PV_TOTAL);
        return value == null ? 0L : Long.parseLong(value);
    }

    public Long getTodayPv() {

        String pvKey = RedisKeyConstants.buildSystemPvKey(LocalDate.now());
        String res = redisService.get(pvKey);
        return res == null ? 0 : Long.parseLong(res);
    }

    public Long getTodayUv() {

        String uvKey = RedisKeyConstants.buildSystemUvKey(LocalDate.now());
        return redisService.sCard(uvKey);
    }

    /**
     * 获取指定日期的 PV / UV
     */
    public Map<String, Long> getStatsForDate(LocalDate date) {
        String pvKey = RedisKeyConstants.buildSystemPvKey(date);
        String uvKey = RedisKeyConstants.buildSystemUvKey(date);
        String pvVal = redisService.get(pvKey);
        long pv = pvVal == null ? 0 : Long.parseLong(pvVal);
        long uv = redisService.sCard(uvKey);
        Map<String, Long> map = new HashMap<>();
        map.put("pv", pv);
        map.put("uv", uv);
        return map;
    }

    /**
     * 获取最近 n 天的 PV / UV 趋势图
     */
    public List<Map<String, Object>> getRecentDaysStats(int days) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            Map<String, Object> dayStats = new HashMap<>();
            Map<String, Long> stats = getStatsForDate(date);
            dayStats.put("date", date.toString());
            dayStats.putAll(stats);
            result.add(dayStats);
        }
        Collections.reverse(result); // 正序输出
        return result;
    }
}
