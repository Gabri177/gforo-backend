package com.yugao.vo.user;

import lombok.Data;

@Data
public class UserInfoVO {

    private Long id;

    private String username;

    private String email;

    private String headerUrl;

    private String bio;

    private Integer status;

    private String createdAt;

    private Long postCount;

    private Long commentCount;
}
