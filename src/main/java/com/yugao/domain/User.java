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
//    username	varchar(50)	NO	UNI
//    password	varchar(100)	YES
//    email	varchar(100)	NO	UNI
//    type	int	YES
//    status	int	YES
//    header_url	varchar(200)	YES
//    create_time	timestamp	YES			on update CURRENT_TIMESTAMP
//    bio	mediumtext	YES
//    nickname	varchar(255)	YES
//    last_username_update_time	timestamp	YES
//    last_email_update_time	timestamp	YES

    @TableId(type= IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String email;

    private Integer type; // 这个变量还没有用到 0-普通用户 1 2 目前默认初始化为0

    private Integer status; // 0-用户状态正常 1-用户状态被封禁

    private String headerUrl;

    private Date createTime;

    private String bio;

    private String nickname;

    private Date lastUsernameUpdateTime;

    private Date lastEmailUpdateTime;
}
