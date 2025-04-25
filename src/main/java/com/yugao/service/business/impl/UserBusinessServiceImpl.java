package com.yugao.service.business.impl;

import com.yugao.converter.UserConverter;
import com.yugao.domain.User;
import com.yugao.dto.UserChangePasswordDTO;
import com.yugao.dto.UserInfoUpdateDTO;
import com.yugao.dto.UserVerifyEmailDTO;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.builder.EmailBuilder;
import com.yugao.service.business.UserBusinessService;
import com.yugao.service.data.UserService;
import com.yugao.service.limiter.EmailRateLimiter;
import com.yugao.service.validator.UserValidator;
import com.yugao.util.mail.MailClientUtil;
import com.yugao.util.security.PasswordUtil;
import com.yugao.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getPrincipal().toString());
    }

    @Override
    public ResponseEntity<ResultFormat> getUserInfo(String token) {

        Long userId = getCurrentUserId();
        User userDomain = userValidator.validateExistenceID(userId);
        UserInfoVO userInfoVO = UserConverter.toVO(userDomain);
        return ResultResponse.success(userInfoVO);
    }

    @Override
    public ResponseEntity<ResultFormat> changePassword(UserChangePasswordDTO userChangePasswordDTO) {
        System.out.println(userChangePasswordDTO);

        Long userId = getCurrentUserId();
        User userDomain = userValidator.validateExistenceID(userId);
        userValidator.validatePasswordChange(userDomain, userChangePasswordDTO);
        String newRawPassword = userChangePasswordDTO.getNewPassword();
        userService.updatePassword(userId, PasswordUtil.encode(newRawPassword));
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> updateUserInfo(UserInfoUpdateDTO userInfoUpdateDTO) {

        Long userId = getCurrentUserId();
        User userDomain = userValidator.validateExistenceID(userId);
        userService.updateUserProfile(userDomain.getId(), userInfoUpdateDTO);
        return ResultResponse.success("userinfo update success");
    }

    @Override
    public ResponseEntity<ResultFormat> sendVerifyEmail(UserVerifyEmailDTO userVerifyEmailDTO) {

        Long userId = getCurrentUserId();
        User user = userValidator.validateUserForEmailVerification(userId, userVerifyEmailDTO);
        emailRateLimiter.check(userVerifyEmailDTO.getEmail());
        String link = emailBuilder.buildActivationLink(user);
        String html = emailBuilder.buildEmailVerifyHtml(user.getUsername(), link);
        mailClient.sendHtmlMail(user.getEmail(), user.getUsername() + ", Please verify your email address", html);
        return ResultResponse.success("Email sent");
    }

    @Override
    public ResponseEntity<ResultFormat> verifyEmail(String userId, String token) {

        Long currentUserId = getCurrentUserId();
        User user = userValidator.validateUserForTokenActivation(currentUserId, userId, token);
        userService.updateStatus(user.getId(), 1);
        return ResultResponse.success("Email verified");
    }
}
