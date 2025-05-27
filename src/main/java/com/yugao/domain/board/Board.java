package com.yugao.domain.board;

import com.baomidou.mybatisplus.annotation.*;
import com.yugao.enums.StatusEnum;
import lombok.Data;

import java.util.Date;

@Data
@TableName("board")
public class Board {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String description;

    private StatusEnum status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String iconUrl;
}
