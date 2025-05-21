package com.yugao.domain.title;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yugao.enums.TitleConditionTypeEnum;
import lombok.Data;

@Data
@TableName("title")
public class Title {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String description;

    private TitleConditionTypeEnum conditionType; // 如："exp", "post_count", "sign_in_days"

    private Integer conditionValue;

    private String iconUrl;

    private Integer buildin; // 是否内置称号，0：否，1：是

    public static Title getDefaultTitle(){
        Title title = new Title();
        title.setId(0L);
        title.setName("unSet");
        title.setDescription("Set your title in profile");
        title.setConditionType(TitleConditionTypeEnum.EXP);
        title.setConditionValue(0);
        title.setIconUrl("");
        title.setBuildin(0);
        return title;
    }
}
