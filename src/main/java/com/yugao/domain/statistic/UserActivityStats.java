package com.yugao.domain.statistic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserActivityStats {

    @TableId(type = IdType.AUTO)
    private Long id;
    private LocalDate statDate;       // 用于日活
    private String statMonth;         // 格式 "2024-05" 用于月活
    private Integer activeUserCount;
    private String type;              // "DAY" 或 "MONTH"
    private LocalDateTime createdTime;
}
