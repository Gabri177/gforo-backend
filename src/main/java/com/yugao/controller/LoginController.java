package com.yugao.controller;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.yugao.domain.User;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.UserService;
import com.yugao.util.ForoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取验证码
     * @return
     */
    @GetMapping()
    public ResponseEntity<ResultFormat> getCaptchaInfo(){

        // 生成验证码
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        specCaptcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);

        // 生成唯一 ID
        String captchaId = UUID.randomUUID().toString();
        String captchaText = specCaptcha.text().toLowerCase();

        // 存入 Redis（有效期 5 分钟）
        redisTemplate.opsForValue().set("captcha:" + captchaId, captchaText, 5, TimeUnit.MINUTES);

        // 返回验证码图片 + captchaId
        Map<String, String> result = new HashMap<>();
        result.put("captchaId", captchaId);
        result.put("captchaBase64", specCaptcha.toBase64());

        return ResultResponse.success(result);
    }

    /**
     * 验证验证码 并登录
     * @param user
     * @param verCode
     * @param captchaId
     * @return
     */
    @PostMapping()
    public ResponseEntity<ResultFormat> login(
            @RequestBody User user,
            @RequestParam("code") String verCode,
            @RequestParam("id") String captchaId) {

        // 从 Redis 取验证码
        String captchaText = redisTemplate.opsForValue().get("captcha:" + captchaId);

        if (captchaText == null || !captchaText.equalsIgnoreCase(verCode.trim())) {
            return ResultResponse.error("Invalid captcha");
        }

        if (user.getPassword() == null || user.getUsername() == null) {
            return ResultResponse.error("Username or password cannot be empty");
        } else {
            User loginUser = userService.getUserByName(user.getUsername());
            if (loginUser == null) {
                return ResultResponse.error("User does not exist");
            }
            String passwd = ForoUtil.md5(user.getPassword() + loginUser.getSalt());
            if (!loginUser.getPassword().equals(passwd)) {
                return ResultResponse.error("Error password");
            }
        }
        // 删除验证码
        redisTemplate.delete("captcha:" + captchaId);
        return ResultResponse.success("Login success");
    }

    /**
     * 切换验证码需要对之前存储的验证码进行删除
     * 节省存储空间
     * @return
     */
    @DeleteMapping("/{captchaId}")
    public ResponseEntity<ResultFormat> logout(@PathVariable String captchaId) {

        redisTemplate.delete("captcha:" + captchaId);
        return ResultResponse.success("删除成功");
    }

}
