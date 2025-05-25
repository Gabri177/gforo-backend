package com.yugao.service.data.report;

import com.yugao.domain.report.Report;
import com.yugao.enums.StatusEnum;

import java.util.List;

public interface ReportService {

    List<Report> getAllReports(Integer cuurentPage, Integer pageSize, StatusEnum status);

    Long countAllReports(StatusEnum status);

    Report getReportById(Long id);

    void updateReport(Report report);

    void addReport(Report report);

    void deleteReport(Long reportId);

    void updateReportStatus(Long reportId, StatusEnum status);
}
