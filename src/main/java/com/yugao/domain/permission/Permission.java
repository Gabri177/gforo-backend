package com.yugao.domain.permission;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yugao.enums.StatusEnum;
import lombok.Data;

import java.util.Date;

@Data
@TableName("permission")
public class Permission {
//    id	bigint	NO	PRI		auto_increment
//    code	varchar(100)	NO	UNI
//    name	varchar(50)	NO
//    description	varchar(255)	YES
//    method	varchar(10)	YES
//    url_pattern	varchar(200)	YES
//    status	tinyint	YES		1
//    create_time	datetime	YES		CURRENT_TIMESTAMP	DEFAULT_GENERATED
//    update_time	datetime	YES		CURRENT_TIMESTAMP	DEFAULT_GENERATED on update CURRENT_TIMESTAMP

    @TableId(type = IdType.AUTO)
    private Long id;

    private String code;

    private String name;

    private String description;

    private String method;

    private String urlPattern;

    private StatusEnum status;

    private Date createTime;

    private Date updateTime;
}
