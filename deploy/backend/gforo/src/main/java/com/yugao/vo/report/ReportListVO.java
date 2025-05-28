package com.yugao.vo.report;

import com.yugao.domain.report.Report;
import lombok.Data;

import java.util.List;

@Data
public class ReportListVO {

    private Integer currentPage; // 当前页码
    private Integer pageSize; // 每页大小
    private Long totalRows;
    private List<ReportInfoVO> reports; // 举报列表
}
