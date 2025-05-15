package com.yugao.vo.user;

import com.yugao.enums.StatusEnum;
import com.yugao.vo.auth.AccessControlVO;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoVO {

    private Long id;

    private String username;

    private String email;

    private String headerUrl;

    private String bio;

    private StatusEnum status;

    private Date createdAt;

    private Long postCount;

    private Long commentCount;

    private String nickname;

    private AccessControlVO accessControl;

    private Integer getLikeCount;
}
