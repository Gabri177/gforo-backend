package com.yugao.controller;

import com.yugao.domain.User;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.UserService;
import com.yugao.util.ForoUtil;
import com.yugao.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 验证是否通过验证 并登录
     * @param user
     * @return
     */
    @PostMapping()
    public ResponseEntity<ResultFormat> login(
            @Validated({ValidationGroups.Login.class}) @RequestBody User user) {

        if (user.getPassword() == null || user.getUsername() == null) {
            return ResultResponse.error("Username or password cannot be empty");
        }

        // 从 Redis 取用户验证状态
        String redisValidateStatusKey = "captcha_verified:" + user.getUsername();
        String redisValidateStatus = redisTemplate.opsForValue().get(redisValidateStatusKey);

        if (redisValidateStatus == null || !redisValidateStatus.equalsIgnoreCase("true")) {
            return ResultResponse.error("Ilegal login");
        }

        User loginUser = userService.getUserByName(user.getUsername());
        if (loginUser == null) {
            return ResultResponse.error("User does not exist");
        }
        String passwd = ForoUtil.md5(user.getPassword() + loginUser.getSalt());
        if (!loginUser.getPassword().equals(passwd)) {
            return ResultResponse.error("Error password");
        }

        // 删除验证码
        redisTemplate.delete(redisValidateStatusKey);
        return ResultResponse.success("Login success");
    }

}
