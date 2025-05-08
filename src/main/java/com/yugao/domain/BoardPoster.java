package com.yugao.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("board_poster")
public class BoardPoster {

//    user_id	bigint	NO	PRI
//    board_id	bigint	NO	PRI

    private Long userId;

    private Long boardId;

    public BoardPoster(Long userId, Long boardId) {
    }
}
