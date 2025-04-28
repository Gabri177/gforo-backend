package com.yugao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.util.Date;

@Data
@TableName("user")
public class User {

//    id	int	NO	PRI		auto_increment
//    username	varchar(50)	YES	MUL
//    password	varchar(100)	YES
//    email	varchar(100)	YES
//    type	int	YES
//    status	int	YES
//    activation_code	varchar(100)	YES
//    header_url	varchar(200)	YES
//    create_time	timestamp	YES			on update CURRENT_TIMESTAMP

    @TableId(type= IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String email;

    private Integer type; // 这个变量还没有用到 0 1 2

    private Integer status; // 这个变量也没有用到 1-启动 0-禁用

    private String activationCode;

    private String headerUrl;

    private Date createTime;

    private String bio;
}
