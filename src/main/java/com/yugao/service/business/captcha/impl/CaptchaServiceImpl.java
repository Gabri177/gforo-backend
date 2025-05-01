package com.yugao.service.business.captcha.impl;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.yugao.constants.RedisKeyConstants;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.base.RedisService;
import com.yugao.service.business.captcha.CaptchaService;
import com.yugao.vo.captcha.CaptchaGraphVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Value("${captcha.expire-time-minutes}")
    private Long captchaExpireTimeMinutes;

    @Value("${captcha.verified-expire-time-minutes}")
    private Long captchaVerifiedExpireTimeMinutes;

    @Autowired
    private RedisService redisService;

    // 判断是否通过验证码
    public boolean verifyCaptcha(String captchaId, String captchaCode) {
        String captchaText = redisService.get(RedisKeyConstants.captcha(captchaId));
        return captchaCode != null && captchaCode.equals(captchaText);
    }

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
        redisService.setTemporarilyByMinutes(RedisKeyConstants.captcha(captchaId),
                captchaText, captchaExpireTimeMinutes);

        // 返回验证码图片 + captchaId
        CaptchaGraphVO captchaGraphVO = new CaptchaGraphVO();
        captchaGraphVO.setCaptchaId(captchaId);
        captchaGraphVO.setCaptchaBase64(specCaptcha.toBase64());

        return ResultResponse.success(captchaGraphVO);
    }

    @Override
    public ResponseEntity<ResultFormat> verifyCaptcha(String verCode, String captchaId, String username, String scene) {
        /**
         * 这里最好不要依赖于requestparam的参数 设置为非必须 自己检查并精细返回错误
         */
        boolean res = verifyCaptcha(captchaId, verCode);
        if (!res) {
            return ResultResponse.error(ResultCode.CAPTCHA_VERIFIED_ERROR);
        }

        // 验证成功后删除验证码
        redisService.delete(RedisKeyConstants.captcha(captchaId));
        // 存储验证码验证状态，设置配置中的分钟数量为有效期
        redisService.setTemporarilyByMinutes(RedisKeyConstants.captchaVerified(scene, username),
                "true", captchaVerifiedExpireTimeMinutes);


        System.out.println("Captcha correct :" +  RedisKeyConstants.captchaVerified(scene, username));
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> deleteCaptcha(String captchaId) {

        // "captcha:" + captchaId
        redisService.delete(RedisKeyConstants.captcha(captchaId));

        return ResultResponse.success(null);
    }

    @Override
    public boolean verifyVerifiedCaptcha(String scene, String username) {
        return redisService.hasKey(RedisKeyConstants.captchaVerified(scene, username)) &&
                "true".equals(redisService.get(RedisKeyConstants.captchaVerified(scene, username)));
    }

    @Override
    public void deleteVerifiedCaptcha(String scene, String username) {
        redisService.delete(RedisKeyConstants.captchaVerified(scene, username));
    }
}
