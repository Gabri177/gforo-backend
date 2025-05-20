package com.yugao.controller.statistic;


import com.yugao.result.ResultFormat;
import com.yugao.service.business.statistic.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<ResultFormat> getDashboardStats() {
        return statisticsService.getDashboardStats();
    }

    @PreAuthorize("hasAnyAuthority('statistics:weekly-activity')")
    @GetMapping("/weekly-activity")
    public ResponseEntity<ResultFormat> getWeeklyActivity() {
        return statisticsService.getLast7DaysActivity();
    }

    @PreAuthorize("hasAnyAuthority('statistics:monthly-registration')")
    @GetMapping("/monthly-registration")
    public ResponseEntity<ResultFormat> getMonthlyRegistration() {
        return statisticsService.getMonthlyRegistrationAndActiveStats();
    }
}
