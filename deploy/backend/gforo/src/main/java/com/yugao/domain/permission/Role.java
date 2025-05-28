package com.yugao.domain.permission;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yugao.enums.StatusEnum;
import lombok.Data;

import java.util.Date;

@Data
@TableName("role")
public class Role {

//    id	bigint	NO	PRI		auto_increment
//    name	varchar(50)	NO	UNI
//    description	varchar(100)	YES
//    status	tinyint	YES		1
//    create_time	datetime	YES		CURRENT_TIMESTAMP	DEFAULT_GENERATED

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String description;

    private StatusEnum status;

    private Date createTime;

    private Integer level;

    private Integer buildin; // 是否内置角色 0:否 1:是
}
