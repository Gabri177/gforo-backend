package com.yugao.domain.title;

import com.baomidou.mybatisplus.annotation.*;
import com.yugao.enums.EntityTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_exp_log")
public class UserExpLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Integer changeValue;

    private String reason;

    private Long relatedId;

    private EntityTypeEnum relatedType;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
