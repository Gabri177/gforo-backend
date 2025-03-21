package com.yugao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_tokens")
public class UserToken {

//    id BIGINT PRIMARY KEY AUTO_INCREMENT,
//    user_id BIGINT NOT NULL,
//    access_token VARCHAR(512) NOT NULL,
//    refresh_token VARCHAR(512) NOT NULL,
//    expires_at DATETIME NOT NULL,  -- 过期时间
//    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//    INDEX idx_user_id (user_id)

    @TableId(type= IdType.AUTO)
    private Long id;

    private int userId;

    private String accessToken;

    private String refreshToken;

    private Date expiresAt;

    private Date createdAt;

    public UserToken() {
    }


}
