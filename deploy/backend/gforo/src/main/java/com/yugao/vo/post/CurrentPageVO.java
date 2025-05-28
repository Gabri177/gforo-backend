package com.yugao.vo.post;

import lombok.Data;

import java.util.List;

@Data
public class CurrentPageVO {

    private Integer currentPage;

    private List<CurrentPageItemVO> discussPosts;

    private Integer limit;

    private Long totalRows;
}
