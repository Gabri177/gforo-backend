package com.yugao.vo.statistics;

import lombok.Data;

@Data
public class MonthlyUserStatsVO {

    private String month; // 格式：2024-05

    private Integer registeredUsers;

    private Integer activeUsers;
}
