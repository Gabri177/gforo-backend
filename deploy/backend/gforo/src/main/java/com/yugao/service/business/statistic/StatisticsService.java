package com.yugao.service.business.statistic;

import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;


public interface StatisticsService {

    ResponseEntity<ResultFormat> getDashboardStats();

    ResponseEntity<ResultFormat> getLast7DaysActivity();

    ResponseEntity<ResultFormat> getMonthlyRegistrationAndActiveStats();

}
