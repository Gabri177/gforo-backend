package com.yugao.service.business.admin.impl;

import com.yugao.domain.user.User;
import com.yugao.dto.admin.ForceChangePasswordDTO;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.builder.VOBuilder;
import com.yugao.service.business.admin.AdminUserService;
import com.yugao.service.data.comment.CommentService;
import com.yugao.service.data.post.DiscussPostService;
import com.yugao.service.data.user.UserService;
import com.yugao.service.handler.PermissionHandler;
import com.yugao.service.validator.UserValidator;
import com.yugao.util.security.PasswordUtil;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.admin.DetailUserInfoPageVO;
import com.yugao.vo.admin.DetailedUserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserService userService;
    private final UserValidator userValidator;
    private final DiscussPostService discussPostService;
    private final CommentService commentService;
    private final VOBuilder voBuilder;
    private final PermissionHandler permissionHandler;


    @Override
    public ResponseEntity<ResultFormat>getUserInfo(Long userId, Integer currentPage, Integer pageSize, Boolean isAsc) {

        Long currentId = SecurityUtils.mustGetLoginUserId();
        Integer roleLevel = permissionHandler.getUserRoleLevel(currentId);
        DetailUserInfoPageVO detailUserInfoPageVO = new DetailUserInfoPageVO();
        List<DetailedUserInfoVO> detailedUserInfoVOS =
                userService.getUsers(userId, currentPage, pageSize, roleLevel)
                        .stream()
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
        detailUserInfoPageVO.setTotalRows(userService.getUserCountWithLowerLevel(userId, roleLevel));
        System.out.println("当前用户ID: " + currentId);
        System.out.println("当前用户等级: " + roleLevel);
        System.out.println("比当前用户身份低的用户数量: " + detailUserInfoPageVO.getTotalRows());
        System.out.println("用户ids" + detailedUserInfoVOS.stream().map(DetailedUserInfoVO::getId).toList());
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

        // 逻辑改变
        return ResultResponse.success(null);
    }
}
