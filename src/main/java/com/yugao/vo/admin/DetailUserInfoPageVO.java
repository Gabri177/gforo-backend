package com.yugao.vo.admin;

import lombok.Data;

import java.util.List;

@Data
public class DetailUserInfoPageVO {

    private Integer currentPage;

    private Integer limit;

    private Long totalRows;

    private List<DetailedUserInfoVO> userInfoList;
}
