package com.yugao.service.data.title;

import com.yugao.domain.title.Title;

import java.util.List;

public interface TitleService {

    Title findBestFitTitleByExp(Integer exp);

    Title findTitleByUserId(Long userId);

    List<Title> getAllTitlesExceptExp();

    List<Title> getTitlesByIds(List<Long> ids);

    Title getTitleById(Long id);

    void addTitle(Title title);

    void delete(Long titleId);

    void updateTitle(Title title);
}
