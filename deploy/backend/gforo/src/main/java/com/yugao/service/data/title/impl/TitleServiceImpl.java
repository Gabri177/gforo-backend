package com.yugao.service.data.title.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yugao.domain.title.Title;
import com.yugao.enums.TitleConditionTypeEnum;
import com.yugao.mapper.title.TitleMapper;
import com.yugao.service.data.title.TitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TitleServiceImpl implements TitleService {

    private final TitleMapper titleMapper;

    @Override
    public Title findBestFitTitleByExp(Integer exp) {

        return titleMapper.selectList(null).stream()
                .filter(t -> TitleConditionTypeEnum.EXP.equals(t.getConditionType()) && exp >= t.getConditionValue())
                .max(Comparator.comparingInt(Title::getConditionValue))
                .orElse(null);
    }

    @Override
    public Title findTitleByUserId(Long userId) {

        return titleMapper.selectTitleByUserId(userId) != null ?
                titleMapper.selectTitleByUserId(userId) :
                Title.getDefaultTitle();
    }

    @Override
    public List<Title> getAllTitlesExceptExp() {

        LambdaQueryWrapper<Title> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(Title::getConditionType, TitleConditionTypeEnum.EXP);
        return titleMapper.selectList(queryWrapper);
    }

    @Override
    public List<Title> getTitlesByIds(List<Long> ids) {

        return titleMapper.selectByIds(ids);
    }

    @Override
    public Title getTitleById(Long id) {

        return titleMapper.selectById(id);
    }

    @Override
    public void addTitle(Title title) {

        titleMapper.insert(title);
    }

    @Override
    public void delete(Long titleId) {

        titleMapper.deleteById(titleId);
    }

    @Override
    public void updateTitle(Title title) {

        titleMapper.updateById(title);
    }
}
