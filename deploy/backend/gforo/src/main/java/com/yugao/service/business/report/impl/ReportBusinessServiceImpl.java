package com.yugao.service.business.report.impl;

import com.yugao.converter.ReportConverter;
import com.yugao.domain.report.Report;
import com.yugao.dto.report.HandleReportDTO;
import com.yugao.dto.report.ReportDTO;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.builder.VOBuilder;
import com.yugao.service.business.report.ReportBusinessService;
import com.yugao.service.data.report.ReportService;
import com.yugao.service.handler.EventHandler;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.report.ReportListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class ReportBusinessServiceImpl implements ReportBusinessService {

    private final ReportService reportService;
    private final VOBuilder voBuilder;
    private final EventHandler eventHandler;

    @Override
    public ResponseEntity<ResultFormat> submitReport(ReportDTO dto) {
        Long curUserId = SecurityUtils.mustGetLoginUserId();
        Report rep = ReportConverter.toReport(dto);
        rep.setReporterId(curUserId);
        reportService.addReport(rep);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> getAllReport(Integer currentPage, Integer pageSize, StatusEnum status) {

        ReportListVO reportListVO = new ReportListVO();
        reportListVO.setCurrentPage(currentPage);
        reportListVO.setPageSize(pageSize);
        reportListVO.setTotalRows(reportService.countAllReports(status));
        reportListVO.setReports(
                voBuilder.assembleReportInfoVOList(reportService.getAllReports(currentPage, pageSize, status))
        );
//        System.out.println("reportListVO = " + reportListVO.getReports());
        return ResultResponse.success(reportListVO);
    }

    @Override
    public ResponseEntity<ResultFormat> handleReport(HandleReportDTO dto) {

        Report rep = reportService.getReportById(dto.getId());
        if (rep == null)
            throw new BusinessException(ResultCodeEnum.REPORT_NOT_FOUND);
        rep.setHandleNote(dto.getHandleNote());
        if (dto.getIsApproved())
            rep.setStatus(StatusEnum.APPROVED);
        else
            rep.setStatus(StatusEnum.REJECTED);

        reportService.updateReport(rep);
        // TODO: 发送系统通知给指定用户
        eventHandler.notifyHandleReport(rep.getReporterId(), rep);
        return ResultResponse.success(null);
    }

}
