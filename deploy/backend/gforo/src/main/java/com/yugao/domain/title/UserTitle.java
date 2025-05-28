package com.yugao.domain.title;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_title")
public class UserTitle {

    private Long userId;

    private Long titleId;

    private LocalDateTime gainTime;
}
