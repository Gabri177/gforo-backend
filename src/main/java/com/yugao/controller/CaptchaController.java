package com.yugao.controller;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.yugao.constants.RedisKeyConstants;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

//    @Autowired
//    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;

    @Value("${captcha.expire-time-minutes}")
    private Long captchaExpireTimeMinutes;

    @Value("${captcha.verified-expire-time-minutes}")
    private Long captchaVerifiedExpireTimeMinutes;

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

        // 存入 Redis（有效期 3 分钟）
        // "captcha:" + captchaId
        //redisTemplate.opsForValue().set(RedisKeyConstants.captchaId(captchaId), captchaText, 3, TimeUnit.MINUTES);
        redisService.setTemporarilyByMinutes(RedisKeyConstants.captcha(captchaId),
                captchaText, captchaExpireTimeMinutes);

        // 返回验证码图片 + captchaId
        Map<String, String> result = new HashMap<>();
        result.put("captchaId", captchaId);
        result.put("captchaBase64", specCaptcha.toBase64());

        return ResultResponse.success(result);
    }

    @PostMapping
    public ResponseEntity<ResultFormat> checkCaptcha(@RequestParam("code") String verCode,
                                                     @RequestParam("id") String captchaId,
                                                     @RequestParam("username") String username,
                                                     @RequestParam("scene") String scene) {
        /**
         * 这里最好不要依赖于requestparam的参数 设置为非必须 自己检查并精细返回错误
         */
        // String redisKey = "captcha:" + captchaId;
        String redisKey = RedisKeyConstants.captcha(captchaId);
        //String redisCaptchaText = redisTemplate.opsForValue().get(redisKey);
        String redisCaptchaText = redisService.get(redisKey);
        if (redisCaptchaText == null) {
            return ResultResponse.error("Captcha expired");
        }
        if (!redisCaptchaText.equals(verCode.toLowerCase())) {
            return ResultResponse.error("Captcha incorrect");
        }

        // 验证成功后删除验证码
        //redisTemplate.delete(redisKey);
        redisService.delete(redisKey);
        // 存储验证码验证状态，设置 3 分钟有效期
        // "captcha_verified:" + username
        //redisTemplate.opsForValue().set(RedisKeyConstants.usernameCaptchaVerified(username), "true", 3, TimeUnit.MINUTES);
        redisService.setTemporarilyByMinutes(RedisKeyConstants.captchaVerified(scene, username),
                "true", captchaVerifiedExpireTimeMinutes);
        System.out.println("Captcha correct :" +  RedisKeyConstants.captchaVerified(scene, username));
        return ResultResponse.success("Captcha correct");
    }

    /**
     * 切换验证码需要对之前存储的验证码进行删除
     * 节省存储空间
     * @return
     */
    @DeleteMapping("/{captchaId}")
    public ResponseEntity<ResultFormat> logout(@PathVariable String captchaId) {

        // "captcha:" + captchaId
        //redisTemplate.delete(RedisKeyConstants.captchaId(captchaId));
        redisService.delete(RedisKeyConstants.captcha(captchaId));
        return ResultResponse.success("删除成功");
    }
}
