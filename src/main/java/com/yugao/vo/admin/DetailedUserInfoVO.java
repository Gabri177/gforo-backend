package com.yugao.vo.admin;

import com.yugao.enums.StatusEnum;
import com.yugao.vo.auth.AccessControlVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DetailedUserInfoVO {

    private Long id;

    private String username;

    private String email;

    private String headerUrl;

    private String bio;

    private Integer type;

    private StatusEnum status;

    private Date createTime;

    private Long postCount;

    private Long commentCount;

    private String nickname;

    private Date lastUsernameUpdateTime;

    private Date lastEmailUpdateTime;

    private AccessControlVO accessControl;

}
