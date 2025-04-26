package com.yugao.vo;

import lombok.Data;

import java.util.List;

@Data
public class PostPageVO {

    private PostDetailVO originalPost; // 原帖信息

    private List<PostDetailVO> replies; // 回复列表

    private Long currentPage;

    private Integer limit;

    private Long totalRows; // 总行数
}
