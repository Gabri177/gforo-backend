package com.yugao.dto.report;

import lombok.Data;

@Data
public class HandleReportDTO {

    private Long id;

    private Boolean isApproved; // 是否通过举报处理

    private String handleNote; // 处理备注
}
