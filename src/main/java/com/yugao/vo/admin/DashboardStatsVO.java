package com.yugao.vo.admin;

import lombok.Data;

@Data
public class DashboardStatsVO {
    private Long totalUsers;
    private Long totalPosts;
    private Long totalComments;
    private Long activeUsers;
    private Long visitToday;
    private Integer newPostsToday;
    private Integer newCommentsToday;
    private Double userGrowthPercent;
    private Double postGrowthPercent;
    private Double commentGrowthPercent;
}

