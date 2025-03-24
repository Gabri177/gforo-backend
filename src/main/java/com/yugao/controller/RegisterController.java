package com.yugao.controller;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.converter.UserConverter;
import com.yugao.domain.User;
import com.yugao.dto.UserRegisterDTO;
import com.yugao.dto.UserVerifyEmailDTO;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.RedisService;
import com.yugao.service.UserService;
import com.yugao.util.MailClientUtil;
import com.yugao.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private MailClientUtil mailClient;

    @Autowired
    private UserService userService;

    @Value("${frontend.url}")
    private String frontend_api;

    @Value("${email.active-account.request-expire-time-minutes}")
    private Long emailRequestTimeInterval;

//    @Autowired
//    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;

    /**
     * 注册用户
     * @param userRegisterDTO
     * @return ResultFormat
     */
    @PostMapping
    public ResponseEntity<ResultFormat> registerAccount(
            @Validated({ValidationGroups.Register.class}) @RequestBody UserRegisterDTO userRegisterDTO) {

        // 检查验证码是否正确
        String redisKey = "captcha_verified:" + userRegisterDTO.getUsername();
        //String redisCaptchaStatus = redisTemplate.opsForValue().get(redisKey);
        String redisCaptchaStatus = redisService.get(redisKey);
        if (redisCaptchaStatus == null || !redisCaptchaStatus.equalsIgnoreCase("true")) {
            return ResultResponse.error(ResultCode.BUSINESS_EXCEPTION,"Illegal registration");
        }
        //redisTemplate.delete(redisKey);
        redisService.delete(redisKey);

        // 直接加入数据库 但是账户是没有验证的状态
        User userDomain = UserConverter.toDomain(userRegisterDTO);

        userService.addUser(userDomain);

        return ResultResponse.success("Register success");
    }

    /**
     * 发送验证邮件
     * @param userVerifyEmailDTO
     * @return
     */
    @PostMapping("/send-verify-email")
    public ResponseEntity<ResultFormat> verifyEmail(
            @Validated @RequestBody UserVerifyEmailDTO userVerifyEmailDTO) {

        String redisKey = RedisKeyConstants.emailRequestAccountActivationEmail(userVerifyEmailDTO.getEmail());
        String redisStatus = redisService.get(redisKey);
        if (redisStatus != null && redisStatus.equalsIgnoreCase("true")) {
            return ResultResponse.error(ResultCode.TOO_SHORT_INTERVAL,"You can't request email verification again in a short time");
        }

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

        redisService.setTemporarilyByMinutes(
                RedisKeyConstants.emailRequestAccountActivationEmail(userVerifyEmailDTO.getEmail()),
                "true",
                emailRequestTimeInterval);

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

        if (token == null || userId == null)
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
