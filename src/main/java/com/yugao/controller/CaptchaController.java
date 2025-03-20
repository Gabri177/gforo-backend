package com.yugao.controller;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

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

    @PostMapping
    public ResponseEntity<ResultFormat> checkCaptcha(@RequestParam("code") String verCode,
                                                     @RequestParam("id") String captchaId,
                                                     @RequestParam("username") String username) {
        String redisKey = "captcha:" + captchaId;
        String redisCaptchaText = redisTemplate.opsForValue().get(redisKey);
        if (redisCaptchaText == null) {
            return ResultResponse.error("Captcha expired");
        }
        if (!redisCaptchaText.equals(verCode.toLowerCase())) {
            return ResultResponse.error("Captcha incorrect");
        }

        // 验证成功后删除验证码
        redisTemplate.delete(redisKey);
        // 存储验证码验证状态，设置 5 分钟有效期
        redisTemplate.opsForValue().set("captcha_verified:" + username, "true", 5, TimeUnit.MINUTES);

        return ResultResponse.success("Captcha correct");
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
