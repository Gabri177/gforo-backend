package com.yugao.service.business.admin.impl;

import com.yugao.domain.User;
import com.yugao.dto.admin.ForceChangePasswordDTO;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.builder.VOBuilder;
import com.yugao.service.business.admin.AdminUserService;
import com.yugao.service.data.*;
import com.yugao.service.validator.UserValidator;
import com.yugao.util.security.PasswordUtil;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.admin.DetailUserInfoPageVO;
import com.yugao.vo.admin.DetailedUserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private VOBuilder voBuilder;


    @Override
    public ResponseEntity<ResultFormat>getUserInfo(Long userId, Integer currentPage, Integer pageSize, Boolean isAsc) {

        Long currentId = SecurityUtils.mustGetLoginUserId();
        DetailUserInfoPageVO detailUserInfoPageVO = new DetailUserInfoPageVO();
        List<DetailedUserInfoVO> detailedUserInfoVOS =
                userService.getUsers(userId, currentPage, pageSize, isAsc)
                        .stream()
                        .filter((item) -> !item.getId().equals(currentId))
                        .map((item) -> {
                            DetailedUserInfoVO detailedUserInfoVO = new DetailedUserInfoVO();
                            BeanUtils.copyProperties(item, detailedUserInfoVO);
                            detailedUserInfoVO.setPostCount(discussPostService.getDiscussPostRows(item.getId(), 0L));
                            detailedUserInfoVO.setCommentCount(commentService.getCommentCountByUserId(item.getId()));
                            detailedUserInfoVO.setAccessControl(voBuilder.buildAccessControlVO(item.getId()));
                            return detailedUserInfoVO;
                        })
                        .toList();
        detailUserInfoPageVO.setCurrentPage(currentPage);
        detailUserInfoPageVO.setLimit(pageSize);
        detailUserInfoPageVO.setTotalRows(userService.getUserCount());
        detailUserInfoPageVO.setUserInfoList(detailedUserInfoVOS);
        return ResultResponse.success(detailUserInfoPageVO);
    }

    @Override
    public ResponseEntity<ResultFormat> changePassword(Long userId, ForceChangePasswordDTO forceChangePasswordDTO) {

        User userDomain = userValidator.validateUserIdExists(userId);
        String newRawPassword = forceChangePasswordDTO.getNewPassword();
        if (!userService.updatePassword(userId, PasswordUtil.encode(newRawPassword)))
            throw new BusinessException(ResultCodeEnum.USER_PASSWORD_UPDATE_ERROR);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> activateUser(Long userId) {

        User domain = userValidator.validateUserIdExists(userId);
        userService.updateStatus(userId, StatusEnum.NORMAL);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> disableUser(Long userId) {

        User domain = userValidator.validateUserIdExists(userId);
        userService.updateStatus(userId, StatusEnum.DISABLED);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> deleteUser(Long userId) {

        User domain = userValidator.validateUserIdExists(userId);
        userService.updateStatus(userId, StatusEnum.DELETED);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> logout(Long userId) {

        Boolean result = userTokenService.deleteUserTokenByUserId(userId);
        if (!result)
            throw new BusinessException(ResultCodeEnum.LOGOUT_WITHOUT_LOGIN);
        return ResultResponse.success(null);
    }
}
