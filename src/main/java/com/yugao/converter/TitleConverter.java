package com.yugao.converter;

import com.yugao.domain.title.Title;
import com.yugao.dto.title.AddTitleDTO;
import com.yugao.enums.TitleConditionTypeEnum;
import com.yugao.vo.title.SimpleTitleVO;
import com.yugao.vo.title.TitleVO;

public class TitleConverter {

    public static TitleVO toTitleVO(Title title){

        TitleVO titleVO = new TitleVO();
        if (title == null) {
            return titleVO;
        }
        titleVO.setId(title.getId());
        titleVO.setName(title.getName());
        titleVO.setDescription(title.getDescription());
        titleVO.setConditionType(title.getConditionType());
        titleVO.setConditionValue(title.getConditionValue());
        titleVO.setIconUrl(title.getIconUrl());
        titleVO.setBuildin(title.getBuildin());
        return titleVO;
    }

    public static SimpleTitleVO toSimpleTitleVO(Title title){
        SimpleTitleVO simpleTitleVO = new SimpleTitleVO();
        if (title == null) {
            return simpleTitleVO;
        }
        simpleTitleVO.setId(title.getId());
        simpleTitleVO.setTitleName(title.getName());
        simpleTitleVO.setTitleDesc(title.getDescription());
        return simpleTitleVO;
    }

    public static Title toTitle(AddTitleDTO dto){
        Title title = new Title();
        title.setName(dto.getName());
        title.setDescription(dto.getDescription());
        title.setConditionType(TitleConditionTypeEnum.SYSTEM);
        return title;
    }

}
