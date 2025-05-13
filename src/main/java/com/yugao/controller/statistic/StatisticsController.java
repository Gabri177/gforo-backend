package com.yugao.controller.statistic;


import com.yugao.result.ResultFormat;
import com.yugao.service.business.statistic.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<ResultFormat> getDashboardStats() {
        return statisticsService.getDashboardStats();
    }
}
