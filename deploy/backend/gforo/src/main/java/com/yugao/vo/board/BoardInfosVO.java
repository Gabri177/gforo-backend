package com.yugao.vo.board;

import lombok.Data;

import java.util.List;

@Data
public class BoardInfosVO {

    private List<BoardInfosItemVO> boardInfos;

    private Long totalCount; // 总数
}
