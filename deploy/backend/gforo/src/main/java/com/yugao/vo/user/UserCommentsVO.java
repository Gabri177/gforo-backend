package com.yugao.vo.user;

import lombok.Data;

import java.util.List;

@Data
public class UserCommentsVO {

    private Integer currentPage;

    private Integer pageSize;

    private Long totalRows;

    private List<UserCommentsItemVO> commentsList;
}
