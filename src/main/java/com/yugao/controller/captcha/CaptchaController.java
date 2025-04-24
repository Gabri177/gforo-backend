package com.yugao.controller.captcha;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.yugao.constants.RedisKeyConstants;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.base.RedisService;
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
        redisService.setCaptchaByMinutes(captchaId, captchaText);

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
        boolean res = redisService.verifyCaptcha(captchaId, verCode);
        if (!res) {
            return ResultResponse.error(ResultCode.CAPTCHA_VERIFIED_ERROR);
        }

        // 验证成功后删除验证码
        redisService.deleteCaptcha(captchaId);
        // 存储验证码验证状态，设置配置中的分钟数量为有效期
        redisService.setVerifiedCaptchaByMinutes(scene, username);


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
        redisService.deleteCaptcha(captchaId);

        return ResultResponse.success("删除成功");
    }
}
