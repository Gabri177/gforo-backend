package com.yugao.vo.auth;

import com.yugao.enums.StatusEnum;
import lombok.Data;

@Data
public class SimplePermissionVO {

    private Long id;

    private String name;

    private String description;

    private StatusEnum status;

    private Integer level;

}
