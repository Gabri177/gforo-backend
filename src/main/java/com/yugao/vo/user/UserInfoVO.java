package com.yugao.vo.user;

import com.yugao.enums.StatusEnum;
import lombok.Data;

@Data
public class UserInfoVO {

    private Long id;

    private String username;

    private String email;

    private String headerUrl;

    private String bio;

    private StatusEnum status;

    private String createdAt;

    private Long postCount;

    private Long commentCount;

    private String nickname;
}
