package com.yugao.service.business.report;

import com.yugao.dto.report.HandleReportDTO;
import com.yugao.dto.report.ReportDTO;
import com.yugao.enums.StatusEnum;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface ReportBusinessService {

    ResponseEntity<ResultFormat> submitReport(ReportDTO dto);

    ResponseEntity<ResultFormat> getAllReport(Integer currentPage, Integer pageSize, StatusEnum status);

    ResponseEntity<ResultFormat> handleReport(HandleReportDTO dto);
}
