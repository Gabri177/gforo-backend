package com.yugao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yugao.validation.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @TableId(type= IdType.AUTO)
    private Long id;

    @NotBlank(message = "Username cannot be empty", groups = {ValidationGroups.DefaultGroup.class})
    @Size(min = 6, max = 20, message = "Username length must be between 6 and 20", groups = {ValidationGroups.DefaultGroup.class})
    private String username;

    @NotBlank(message = "Password cannot be empty", groups = {ValidationGroups.DefaultGroup.class})
    @Size(min = 6, max = 20, message = "Password length must be between 6 and 20", groups = {ValidationGroups.DefaultGroup.class})
    private String password;

    private String salt;

    @NotBlank(message = "Email cannot be empty", groups = {ValidationGroups.Register.class})
    @Email(message = "Email format is incorrect", groups = {ValidationGroups.Register.class})
    private String email;

    private Integer type;

    private Integer status;

    private String activationCode;

    private String headerUrl;

    private Date createTime;
}
