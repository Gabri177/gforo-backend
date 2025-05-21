package com.yugao.vo.title;

import com.yugao.enums.TitleConditionTypeEnum;
import lombok.Data;

@Data
public class TitleVO {

    private Long id;

    private String name;

    private String description;

    private TitleConditionTypeEnum conditionType; // 如："exp", "post_count", "sign_in_days"

    private Integer conditionValue;

    private String iconUrl;

    private Integer buildin;
}
