package com.yugao.domain.user;

import com.baomidou.mybatisplus.annotation.*;

import com.yugao.enums.StatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

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

    private Integer expPoints;

    private Long titleId;

    private StatusEnum status;

    private String headerUrl;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String bio;

    private String nickname;

    private Date lastUsernameUpdateTime;

    private Date lastEmailUpdateTime;

    public static User createGhostUser() {
        User user = new User();
        int randomNum = ThreadLocalRandom.current().nextInt(1, 999);
        String randomStr = String.valueOf(randomNum);
        user.setId(-1L);
        user.setUsername("Ghost" + randomStr);
        user.setEmail("GhostEmail" + randomStr + "@example.com");
        user.setExpPoints(0);
        user.setStatus(StatusEnum.NORMAL);
        user.setHeaderUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnpG1ZOxWU7_lCGM2Szc9IUKX9s0vkUDGnng&s");
        user.setCreateTime(new Date());
        user.setBio("this is ghost user");
        user.setNickname("Ghost" + randomStr);
        return user;
    }
}
