package com.yugao.converter;

import com.yugao.domain.report.Report;
import com.yugao.dto.report.ReportDTO;
import com.yugao.enums.StatusEnum;

import java.util.Date;

public class ReportConverter {

    public static Report toReport(ReportDTO reportDTO) {
        if (reportDTO == null) {
            return null;
        }

        Report report = new Report();
        report.setTargetType(reportDTO.getTargetType());
        report.setTargetId(reportDTO.getTargetId());
        report.setReason(reportDTO.getReason());
        report.setStatus(StatusEnum.PENDING);
        report.setCreateTime(new Date());
        return report;
    }
}
