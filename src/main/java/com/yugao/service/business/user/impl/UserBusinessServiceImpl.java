package com.yugao.service.business.user.impl;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.converter.UserConverter;
import com.yugao.domain.User;
import com.yugao.dto.auth.ActiveAccountDTO;
import com.yugao.dto.user.UserChangePasswordDTO;
import com.yugao.dto.user.UserChangeUsernameDTO;
import com.yugao.dto.user.UserInfoUpdateDTO;
import com.yugao.dto.auth.UserVerifyEmailDTO;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.builder.EmailBuilder;
import com.yugao.service.builder.VOBuilder;
import com.yugao.service.business.captcha.CaptchaService;
import com.yugao.service.business.user.UserBusinessService;
import com.yugao.service.data.*;
import com.yugao.service.limiter.EmailRateLimiter;
import com.yugao.service.validator.CaptchaValidator;
import com.yugao.service.validator.UserValidator;
import com.yugao.util.mail.MailClientUtil;
import com.yugao.util.security.PasswordUtil;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.user.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserBusinessServiceImpl implements UserBusinessService {

    @Autowired
    private UserService userService;

    @Autowired
    private MailClientUtil mailClient;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private EmailRateLimiter emailRateLimiter;

    @Autowired
    private EmailBuilder emailBuilder;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CaptchaValidator captchaValidator;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private VOBuilder voBuilder;


    @Override
    public ResponseEntity<ResultFormat> getUserInfo(Long userId) {

        Long curUserId = SecurityUtils.mustGetLoginUserId();
        if (userId != null && !userId.equals(curUserId))
            curUserId = userId;
        User userDomain = userValidator.validateUserIdExists(curUserId);
        UserInfoVO userInfoVO;
        if (SecurityUtils.mustGetLoginUserId().equals(curUserId))
            userInfoVO = UserConverter.toUserInfoVO(userDomain, false);
        else
            userInfoVO = UserConverter.toUserInfoVO(userDomain, true);
        userInfoVO.setPostCount(discussPostService.getDiscussPostRows(curUserId, 0L));
        userInfoVO.setCommentCount(commentService.getCommentCountByUserId(curUserId));
        userInfoVO.setAccessControl(voBuilder.buildAccessControlVO(curUserId));
//        System.out.println(userInfoVO);
        return ResultResponse.success(userInfoVO);
    }

    @Override
    public ResponseEntity<ResultFormat> changePassword(UserChangePasswordDTO userChangePasswordDTO) {
        System.out.println(userChangePasswordDTO);

        Long userId = SecurityUtils.mustGetLoginUserId();
        User userDomain = userValidator.validateUserIdExists(userId);
        userValidator.validatePasswordChange(userDomain, userChangePasswordDTO);
        String newRawPassword = userChangePasswordDTO.getNewPassword();
        if (!userService.updatePassword(userId, PasswordUtil.encode(newRawPassword)))
            throw new BusinessException(ResultCodeEnum.USER_PASSWORD_UPDATE_ERROR);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> updateUserInfo(UserInfoUpdateDTO userInfoUpdateDTO) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        User userDomain = userValidator.validateUserIdExists(userId);
        if(!userService.updateUserProfile(userDomain.getId(), userInfoUpdateDTO))
            throw new BusinessException(ResultCodeEnum.USER_PROFILE_UPDATE_ERROR);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> sendVerifyEmail(UserVerifyEmailDTO userVerifyEmailDTO) {

//        System.out.println("userVerifyEmailDTO = " + userVerifyEmailDTO);
        Long currentUserId = SecurityUtils.mustGetLoginUserId();
        userValidator.validateEmailChangeInterval(currentUserId);
        emailRateLimiter.check(userVerifyEmailDTO.getEmail());
        String code = captchaService.generateSixDigitCaptcha(
                RedisKeyConstants.CHANGE_EMAIL,
                userVerifyEmailDTO.getEmail());
        System.out.println("code = " + code);
        String html = emailBuilder.buildSixCodeVerifyHtml(code);
        mailClient.sendHtmlMail(
                userVerifyEmailDTO.getEmail(),
                "GForo: Change Email",
                html
        );
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> verifyEmail(ActiveAccountDTO activeAccountDTO) {

        System.out.println("activeAccountDTO = " + activeAccountDTO);
        Long currentUserId = SecurityUtils.mustGetLoginUserId();
        captchaValidator.validateSixDigitCaptcha(
                RedisKeyConstants.CHANGE_EMAIL,
                activeAccountDTO.getEmail(),
                activeAccountDTO.getSixDigitCaptcha());
        userValidator.validateUserIdExists(currentUserId);
        if (!userService.updateEmail(currentUserId, activeAccountDTO.getEmail()))
            throw new BusinessException(ResultCodeEnum.USER_EMAIL_UPDATE_ERROR);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> changeUsername(UserChangeUsernameDTO userChangeUsernameDTO) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        userValidator.validateUsernameChangeInterval(userId);
        if (!userService.updateUsername(userId, userChangeUsernameDTO.getUsername()))
            throw new BusinessException(ResultCodeEnum.USER_USERNAME_UPDATE_ERROR);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> logout() {

        Long userId = SecurityUtils.mustGetLoginUserId();
        Boolean isLogout = userTokenService.deleteUserTokenByUserId(userId);
        System.out.println("isLogout: " + isLogout);
        if (!isLogout) {
            throw new BusinessException(ResultCodeEnum.LOGOUT_WITHOUT_LOGIN);
        }
        return ResultResponse.success(null);
    }
}
