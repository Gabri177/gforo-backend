package com.yugao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    private Date createTime;

    private String iconUrl;
}
