package com.yugao.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user")
public class User {

//    id	int	NO	PRI		auto_increment
//    username	varchar(50)	YES	MUL
//    password	varchar(50)	YES
//    salt	varchar(50)	YES
//    email	varchar(100)	YES
//    type	int	YES
//    status	int	YES
//    activation_code	varchar(100)	YES
//    header_url	varchar(200)	YES
//    create_time	timestamp	YES			on update CURRENT_TIMESTAMP

    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private Integer type;
    private Integer status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}
