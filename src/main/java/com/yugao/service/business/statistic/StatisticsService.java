package com.yugao.service.business.statistic;

import com.yugao.result.ResultFormat;
import com.yugao.vo.statistics.DailyActivityVO;
import com.yugao.vo.statistics.MonthlyUserStatsVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StatisticsService {

    ResponseEntity<ResultFormat> getDashboardStats();

    ResponseEntity<ResultFormat> getLast7DaysActivity();

    ResponseEntity<ResultFormat> getMonthlyRegistrationAndActiveStats();

}
