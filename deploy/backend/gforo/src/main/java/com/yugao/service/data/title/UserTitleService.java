package com.yugao.service.data.title;

import com.yugao.domain.title.UserTitle;

import java.util.List;

public interface UserTitleService {

    void addUserTitle(UserTitle userTitle);

    void addUserTitle(Long userId, Long titleId);

    Long getUserTitlesCount(Long userId);

    List<Long> getTitleIdsByUserId(Long userId);

    void deleteUserTitleByTitleId(Long titleId);
}
