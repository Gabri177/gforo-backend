package com.yugao.service.business.title.impl;

import com.yugao.converter.TitleConverter;
import com.yugao.domain.title.UserExpLog;
import com.yugao.domain.title.Title;
import com.yugao.domain.user.User;
import com.yugao.dto.title.AddTitleDTO;
import com.yugao.dto.title.UpdateTitleDTO;
import com.yugao.enums.EntityTypeEnum;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.title.TitleBusinessService;
import com.yugao.service.data.title.*;
import com.yugao.service.data.user.UserService;
import com.yugao.service.handler.EventHandler;
import com.yugao.service.validator.UserValidator;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.title.TitleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TitleBusinessServiceImpl implements TitleBusinessService {

    private final UserService userService;
    private final UserExpLogService userExpLogService;
    private final UserTitleService userTitleService;
    private final TitleService titleService;
    private final EventHandler eventHandler;
    private final UserValidator userValidator;

    @Override
    public void addExp(Long userId, int value, String reason, EntityTypeEnum relatedType, Long relatedId) {
        UserExpLog log = new UserExpLog();
        log.setUserId(userId);
        log.setChangeValue(value);
        log.setReason(reason);
        log.setRelatedId(relatedId);
        log.setRelatedType(relatedType);
        userExpLogService.addExpLog(log);
        userService.increaseExp(userId, value);
        refreshUserExpTitle(userId);
    }

    @Override
    public void subtractExp(Long userId, int value, String reason, EntityTypeEnum relatedType, Long relatedId) {
        UserExpLog log = new UserExpLog();
        log.setUserId(userId);
        log.setChangeValue(-value);
        log.setReason(reason);
        log.setRelatedId(relatedId);
        log.setRelatedType(relatedType);
        userExpLogService.addExpLog(log);
        userService.decreaseExp(userId, value);
        refreshUserExpTitle(userId);
    }

    @Override
    public ResponseEntity<ResultFormat> getUserTitleList(Long userId) {

        if (userId == null)
            userId = SecurityUtils.mustGetLoginUserId();

        userValidator.validateUserIdExists(userId);
        refreshUserExpTitle(userId);
        List<Long> titleIds = userTitleService.getTitleIdsByUserId(userId);
        System.out.println(titleIds);
        List<TitleVO> titles = titleService.getTitlesByIds(titleIds)
                .stream()
                .map(TitleConverter::toTitleVO)
                .toList();
        return ResultResponse.success(titles);
    }

    @Override
    public ResponseEntity<ResultFormat> getTitleListWithoutExp() {

        List<Title> res = titleService.getAllTitlesExceptExp();
        return ResultResponse.success(res.stream().map(TitleConverter::toTitleVO).toList());
    }

    @Override
    public ResponseEntity<ResultFormat> setUserTitle(Long titleId) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        if (!userTitleService.getTitleIdsByUserId(userId).contains(titleId)) {
            throw new BusinessException(ResultCodeEnum.TITLE_NOT_GRANTED);
        }
        User curUser = userService.getUserById(userId);
        curUser.setTitleId(titleId);
        userService.updateUser(curUser);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> updateTitle(UpdateTitleDTO updateTitleDTO) {

        Title title = titleService.getTitleById(updateTitleDTO.getId());
        if (title == null)
            throw new BusinessException(ResultCodeEnum.TITLE_NOT_EXIST);
        title.setName(updateTitleDTO.getName());
        title.setDescription(updateTitleDTO.getDescription());
        titleService.updateTitle(title);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> deleteTitle(Long titleId) {

        Title title = titleService.getTitleById(titleId);
        if (title == null)
            throw new BusinessException(ResultCodeEnum.TITLE_NOT_EXIST);
        if (title.getBuildin() == 1)
            throw new BusinessException(ResultCodeEnum.TITLE_BUILTIN);
        // 删除称号
        titleService.delete(titleId);
        // 删除用户称号
        userTitleService.deleteUserTitleByTitleId(titleId);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> grantTitle(Long userId, Long titleId) {

        Title title = titleService.getTitleById(titleId);
        if (title == null)
            throw new BusinessException(ResultCodeEnum.TITLE_NOT_EXIST);
        userValidator.validateUserIdExists(userId);
        // 写入用户称号表
        userTitleService.addUserTitle(userId, titleId);
        // 发送称号变更事件
        eventHandler.notifyNewTitle(userId, titleId);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> addTitle(AddTitleDTO addTitleDTO) {

        titleService.addTitle(TitleConverter.toTitle(addTitleDTO));
        return ResultResponse.success(null);
    }

    @Override
    public void refreshUserExpTitle(Long userId) {
        User user = userService.getUserById(userId);
        int exp = user.getExpPoints();

        Title bestFit = titleService.findBestFitTitleByExp(exp);
        List<Long> titleIds = userTitleService.getTitleIdsByUserId(userId);

        if (!titleIds.contains(bestFit.getId())) {
            // 写入用户称号表
            userTitleService.addUserTitle(userId, bestFit.getId());
            // 发送称号变更事件
            eventHandler.notifyNewTitle(userId, bestFit.getId());
        }
    }
}
