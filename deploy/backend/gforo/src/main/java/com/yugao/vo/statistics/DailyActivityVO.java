package com.yugao.vo.statistics;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyActivityVO {

    private LocalDate date; // 日期（yyyy-MM-dd）

    private Integer activeUsers;

    private Integer postCount;

    private Integer commentCount;
}
