package com.yugao.vo.user;

import com.yugao.enums.StatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OtherUserInfoVO {

    private Long id;
    private String username;
    private String headerUrl;
    private String nickname;
    private String bio;
    private String email;
    private StatusEnum status;
    private Long postCount;
    private Long commentCount;
    private Date createdAt;
    private List<String> roles;
    private List<Long> boardIds;
}
