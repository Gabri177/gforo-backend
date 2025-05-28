package com.yugao.controller.report;

import com.yugao.dto.report.HandleReportDTO;
import com.yugao.dto.report.ReportDTO;
import com.yugao.enums.StatusEnum;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.report.ReportBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportBusinessService reportBusinessService;

    @PreAuthorize("hasAnyAuthority('report:submit:own')")
    @PostMapping("/submit")
    public ResponseEntity<ResultFormat> submitReport(
            @Validated @RequestBody ReportDTO dto
            ){
        return reportBusinessService.submitReport(dto);
    }

    @PreAuthorize("hasAnyAuthority('report:info:any')")
    @GetMapping("/all")
    public ResponseEntity<ResultFormat> getAllReport(
            @RequestParam (value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam (value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam (value = "status", defaultValue = "6") Integer status
    ){

        return reportBusinessService.getAllReport(currentPage, pageSize, StatusEnum.fromValue(status));
    }

    @PreAuthorize("hasAnyAuthority('report:handle:any')")
    @PutMapping("/handle")
    public ResponseEntity<ResultFormat> handleReport(
            @Validated @RequestBody HandleReportDTO dto
            ){
        return reportBusinessService.handleReport(dto);
    }
}
