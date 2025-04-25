package com.yugao.service.business.impl;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.yugao.constants.RedisKeyConstants;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.base.RedisService;
import com.yugao.service.business.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private RedisService redisService;

    @Override
    public ResponseEntity<ResultFormat> generateCaptcha() {
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

    @Override
    public ResponseEntity<ResultFormat> verifyCaptcha(String verCode, String captchaId, String username, String scene) {
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

    @Override
    public ResponseEntity<ResultFormat> deleteCaptcha(String captchaId) {

        // "captcha:" + captchaId
        redisService.deleteCaptcha(captchaId);

        return ResultResponse.success("删除成功");
    }
}
