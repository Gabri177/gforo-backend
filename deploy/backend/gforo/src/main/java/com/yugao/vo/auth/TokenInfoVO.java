package com.yugao.vo.auth;

import lombok.Data;

@Data
public class TokenInfoVO {

    private String access_token;
    private String refresh_token;
}
