package com.yugao.domain.report;

import com.baomidou.mybatisplus.annotation.*;
import com.yugao.enums.EntityTypeEnum;
import com.yugao.enums.StatusEnum;
import lombok.Data;

import java.util.Date;

@Data
@TableName("report")
public class Report {

//    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '举报记录主键',
//    reporter_id BIGINT NOT NULL COMMENT '举报人用户ID',
//    target_type TINYINT NOT NULL COMMENT '举报对象类型（post/comment）',
//    target_id BIGINT NOT NULL COMMENT '被举报对象的ID',
//    reason TEXT COMMENT '举报详细说明',
//    status TINYINT NOT NULL DEFAULT 0 COMMENT '举报状态（0待处理，1已处理，2驳回）',
//    handler_id BIGINT DEFAULT NULL COMMENT '处理人ID',
//    handle_note TEXT COMMENT '处理备注',
//    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间',
//    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间/处理时间',
//
//    INDEX idx_reporter (reporter_id),
//    INDEX idx_target (target_type, target_id),
//    INDEX idx_status (status)

    @TableId(type = IdType.AUTO)
    private Long id; // 举报记录主键

    private Long reporterId; // 举报人用户ID
    private EntityTypeEnum targetType; // 举报对象类型（post/comment）
    private Long targetId; // 被举报对象的ID
    private String reason; // 举报详细说明
    private StatusEnum status; // 举报状态
    private Long handlerId; // 处理人ID
    private String handleNote; // 处理备注

    @TableField(fill = FieldFill.INSERT)
    private Date createTime; // 举报时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime; // 更新时间/处理时间
}
