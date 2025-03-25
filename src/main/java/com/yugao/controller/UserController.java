package com.yugao.controller;

import com.yugao.converter.UserConverter;
import com.yugao.domain.User;
import com.yugao.dto.UserChangePasswordDTO;
import com.yugao.dto.UserInfoUpdateDTO;
import com.yugao.dto.UserVerifyEmailDTO;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.base.RedisService;
import com.yugao.service.data.UserService;
import com.yugao.util.MailClientUtil;
import com.yugao.util.PasswordUtil;
import com.yugao.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailClientUtil mailClient;

    @Autowired
    private RedisService redisService;

    @Value("${frontend.url}")
    private String frontend_api;

    @GetMapping("/info")
    public ResponseEntity<ResultFormat> getUserInfo(@RequestHeader("Authorization") String token) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getPrincipal().toString());

        User userDomain = userService.getUserById(userId);

        if (userDomain == null) {
            return ResultResponse.error(ResultCode.USER_NOT_FOUND, "user not found");
        }

        UserInfoVO userInfoVO = UserConverter.toVO(userDomain);
        return ResultResponse.success(userInfoVO, "userinfo get success");
    }

    @PutMapping ("/change-password")
    public ResponseEntity<ResultFormat> changePasswoed(
            @Validated @RequestBody UserChangePasswordDTO userChangePasswordDTO) {

        System.out.println(userChangePasswordDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getPrincipal().toString());

        User userDomain = userService.getUserById(userId);

        if (userDomain == null) {
            return ResultResponse.error(ResultCode.USER_NOT_FOUND, "user not found");
        }

        if (userChangePasswordDTO.getOldPassword().equals(userChangePasswordDTO.getNewPassword())) {
            return ResultResponse.error(ResultCode.NEW_PASSWORD_SAME, "two password is the same");
        }

        String oldRawPassword = userChangePasswordDTO.getOldPassword();
        String newRawPassword = userChangePasswordDTO.getNewPassword();
        String currentPassword = userDomain.getPassword();
        if (!PasswordUtil.matches(oldRawPassword, currentPassword)) {
            return ResultResponse.error(ResultCode.OLD_PASSWORD_INCORRECT, "old password is error");
        }

        if (PasswordUtil.matches(newRawPassword, currentPassword)) {
            return ResultResponse.error(ResultCode.NEW_PASSWORD_SAME, "new password is the same as old password");
        }

        userService.updatePassword(userId, PasswordUtil.encode(newRawPassword));
        return ResultResponse.success("password change success");
    }

    @PutMapping("/info")
    public ResponseEntity<ResultFormat> updateUserInfo(
            @Validated @RequestBody UserInfoUpdateDTO userInfoUpdateDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getPrincipal().toString());

        User userDomain = userService.getUserById(userId);

        if (userDomain == null) {
            return ResultResponse.error(ResultCode.USER_NOT_FOUND, "user not found");
        }

        userService.updateUserProfile(userDomain.getId(), userInfoUpdateDTO);

        return ResultResponse.success("userinfo update success");
    }


    /**
     * 发送验证邮件
     * @param userVerifyEmailDTO
     * @return
     */
    @PostMapping("/send-verify-email")
    public ResponseEntity<ResultFormat> verifyEmail(
            @Validated @RequestBody UserVerifyEmailDTO userVerifyEmailDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getPrincipal().toString());

        if (!userId.equals(userVerifyEmailDTO.getId())) {
            return ResultResponse.error(ResultCode.USER_INFO_INVALID,"You can't request email verification for other user");
        }

        boolean res = redisService.verifyEmailActivationInterval(userVerifyEmailDTO.getEmail());
        if (res) {
            return ResultResponse.error(ResultCode.TOO_SHORT_INTERVAL,"You can't request email verification again in a short time");
        }
        redisService.deleteEmailActivationInterval(userVerifyEmailDTO.getEmail());

        System.out.println("Verifying email: " + userVerifyEmailDTO);
        User existUser = userService.getUserById(userVerifyEmailDTO.getId());
        if (existUser == null) {
            return ResultResponse.error(ResultCode.USER_NOT_FOUND,"User not found");
        }
        if (!existUser.getUsername().equals(userVerifyEmailDTO.getUsername()) ||
                !existUser.getEmail().equals(userVerifyEmailDTO.getEmail())) {
            return ResultResponse.error(ResultCode.USER_INFO_INVALID,"Invalid user information");
        }
        if (existUser.getStatus() == 1) {
            return ResultResponse.error(ResultCode.USER_ALREADY_VERIFIED,"Email already verified");
        }

        redisService.setEmailActivationIntervalByMinutes(userVerifyEmailDTO.getEmail());

        System.out.println("Email sending to: " + existUser.getEmail());
        // 发送邮件 用于验证邮箱 这一部分未来可能抽象出来 用于想未来验证邮箱的人群
        // 因为主键自动回填的机制 所以不需要再从数据库取出user来读取Id
        // 注意有两种方式 一种是通过xml配置 一种是通过注解
        // user = userService.getUserByEmail(user.getEmail());

        String verifyLink = frontend_api + "/register/" +
                existUser.getId() + "/" + existUser.getActivationCode();

        String htmlContent = "<p>Hi " + existUser.getUsername() + ",</p>" +
                "<p>Please click the link below to verify your email address:</p>" +
                "<p><a href='" + verifyLink + "' style='color: #4A90E2;'>Verify Email</a></p>" +
                "<p>If the button doesn't work, copy this link into your browser:</p>" +
                "<p>" + verifyLink + "</p>";

        mailClient.sendHtmlMail(
                existUser.getEmail(),
                existUser.getUsername() + ", Please verify your email address",
                htmlContent
        );
        System.out.println("Email Verify Send to user: " + existUser);
        return ResultResponse.success("Email sent");
    }

    /**
     * 当前端打开验证链接 前端会将token作为参数传递给后端 验证token是否正确
     * @param userId
     * @param token
     * @return
     */
    @GetMapping("/verify-email/{userId}/{token}")
    public ResponseEntity<ResultFormat> verifyEmail(@PathVariable String userId, @PathVariable String token) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = Long.parseLong(authentication.getPrincipal().toString());

        if (token == null || userId == null)
            return ResultResponse.error(ResultCode.TOKEN_INVALID,"Invalid token");
        if (!userId.equals(currentUserId.toString()))
            return ResultResponse.error(ResultCode.TOKEN_INVALID,"Invalid token");
        User user = userService.getUserById(Long.parseLong(userId));
        if (user == null)
            return ResultResponse.error(ResultCode.USER_NOT_FOUND,"User not found");
        if (user.getStatus() == 1)
            return ResultResponse.error(ResultCode.EMAIL_ALREADY_VERIFIED,"Email already verified");

        System.out.println("Verifying email with token: " + token);
        System.out.println("User id: " + userId);
        if (!token.equals(user.getActivationCode())){
            System.out.println("Invalid token");
            return ResultResponse.error(ResultCode.TOKEN_INVALID,"Invalid token");
        } else {
            System.out.println("Email verified");
            userService.updateStatus(user.getId(), 1);
            return ResultResponse.success("Email verified");
        }
    }

}
