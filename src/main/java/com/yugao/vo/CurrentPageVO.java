package com.yugao.vo;

import lombok.Data;

import java.util.List;

@Data
public class CurrentPageVO {

    private Long currentPage;

    private List<CurrentPageItemVO> discussPosts;

    private Integer limit;

    private Long totalRows;
}
