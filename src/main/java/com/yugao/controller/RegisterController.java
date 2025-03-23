package com.yugao.controller;

import com.yugao.converter.UserConverter;
import com.yugao.domain.User;
import com.yugao.dto.UserDTO;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.RedisService;
import com.yugao.service.UserService;
import com.yugao.util.MailClientUtil;
import com.yugao.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
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

//    @Autowired
//    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;

    /**
     * 注册用户
     * @param userDTO
     * @return ResultFormat
     */
    @PostMapping
    public ResponseEntity<ResultFormat> handleRegister(@Validated({ValidationGroups.Register.class}) @RequestBody UserDTO userDTO) {

        // 检查验证码是否正确
        String redisKey = "captcha_verified:" + userDTO.getUsername();
        //String redisCaptchaStatus = redisTemplate.opsForValue().get(redisKey);
        String redisCaptchaStatus = redisService.get(redisKey);
        if (redisCaptchaStatus == null || !redisCaptchaStatus.equalsIgnoreCase("true")) {
            return ResultResponse.error(ResultCode.BUSINESS_EXCEPTION,"Illegal registration");
        }
        //redisTemplate.delete(redisKey);
        redisService.delete(redisKey);

        // 直接加入数据库 但是账户是没有验证的状态
        User userDomain = UserConverter.toDomain(userDTO);

        userService.addUser(userDomain);

        // 发送邮件 用于验证邮箱 这一部分未来可能抽象出来 用于想未来验证邮箱的人群
        // 因为主键自动回填的机制 所以不需要再从数据库取出user来读取Id
        // 注意有两种方式 一种是通过xml配置 一种是通过注解
        // user = userService.getUserByEmail(user.getEmail());
        mailClient.sendMail(userDomain.getEmail(),
                    userDomain.getUsername() + ", Please verify your email address",
                    "Click the link to verify your email:\n\t\t\t" +
                            frontend_api + "/register/" +
                            userDomain.getId() + "/" +
                            userDomain.getActivationCode());
        System.out.println("Registering user: " + userDomain);
        return ResultResponse.success("Email sent");
    }

    /**
     * 当前端打开验证链接 前端会将token作为参数传递给后端 验证token是否正确
     * @param userId
     * @param token
     * @return
     */
    @GetMapping("/{userId}/{token}")
    public ResponseEntity<ResultFormat> verifyEmail(@PathVariable String userId, @PathVariable String token) {

        if (token == null || userId == null)
            return ResultResponse.error(ResultCode.BUSINESS_EXCEPTION,"Invalid token");
        User user = userService.getUserById(Long.parseLong(userId));
        if (user == null)
            return ResultResponse.error(ResultCode.BUSINESS_EXCEPTION,"User not found");

        System.out.println("Verifying email with token: " + token);
        System.out.println("User id: " + userId);
        if (!token.equals(user.getActivationCode())){
            System.out.println("Invalid token");
            return ResultResponse.error(ResultCode.BUSINESS_EXCEPTION,"Invalid token");
        } else {
            System.out.println("Email verified");
            userService.updateStatus(user.getId(), 1);
            return ResultResponse.success("Email verified");
        }
    }
}
