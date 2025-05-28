package com.yugao.vo.auth;

import lombok.Data;

import java.util.List;

@Data
public class AccessControlVO {

    private List<String> roles;
    private List<String> permissions;
    private List<Long>   boardIds;
}
