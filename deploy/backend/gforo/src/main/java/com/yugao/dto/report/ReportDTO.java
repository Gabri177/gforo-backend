package com.yugao.dto.report;

import com.yugao.enums.EntityTypeEnum;
import lombok.Data;

@Data
public class    ReportDTO {

    private EntityTypeEnum targetType; // 举报对象类型（post/2comment）
    private Long targetId; // 被举报对象的ID
    private String reason; // 举报详细说明
}
