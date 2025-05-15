package com.yugao.service.business.captcha.impl;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.yugao.constants.RedisKeyConstants;
import com.yugao.dto.captcha.GraphCaptchaDTO;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.base.RedisService;
import com.yugao.service.business.captcha.CaptchaService;
import com.yugao.util.captcha.VerificationUtil;
import com.yugao.vo.captcha.CaptchaGraphVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    @Value("${captcha.sixDigVerifyCodeExpireTimeMinutes}")
    private long sixDigVerifyCodeExpireTimeMinutes;

    @Value("${captcha.expire-time-minutes}")
    private Long captchaExpireTimeMinutes;

    @Value("${captcha.verified-expire-time-minutes}")
    private Long captchaVerifiedExpireTimeMinutes;

    private final RedisService redisService;

    // 判断是否通过验证码
    public boolean verifyGraphCaptcha(String captchaId, String captchaCode) {
        String captchaText = redisService.get(RedisKeyConstants.buildGraphCaptchaKey(captchaId));
        return captchaCode != null && captchaCode.equals(captchaText);
    }

    @Override
    public ResponseEntity<ResultFormat> generateGraphCaptcha() {
        // 生成验证码
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        specCaptcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);

        // 生成唯一 ID
        String captchaId = UUID.randomUUID().toString();
        String captchaText = specCaptcha.text().toLowerCase();

        // 存入 Redis（有效期 3 分钟）
        // "captcha:" + captchaId
        redisService.setTemporarilyByMinutes(RedisKeyConstants.buildGraphCaptchaKey(captchaId),
                captchaText, captchaExpireTimeMinutes);

        // 返回验证码图片 + captchaId
        CaptchaGraphVO captchaGraphVO = new CaptchaGraphVO();
        captchaGraphVO.setCaptchaId(captchaId);
        captchaGraphVO.setCaptchaBase64(specCaptcha.toBase64());

        return ResultResponse.success(captchaGraphVO);
    }

    @Override
    public ResponseEntity<ResultFormat> verifyGraphCaptcha(GraphCaptchaDTO dto) {

        boolean res = verifyGraphCaptcha(dto.getCaptchaId(), dto.getVerCode());
        if (!res) {
            return ResultResponse.error(ResultCodeEnum.CAPTCHA_VERIFIED_ERROR);
        }

        // 验证成功后删除验证码
        redisService.delete(RedisKeyConstants.buildGraphCaptchaKey(dto.getCaptchaId()));
        // 存储验证码验证状态，设置配置中的分钟数量为有效期
        redisService.setTemporarilyByMinutes(RedisKeyConstants.buildGraphCaptchaVerifiedKey(dto.getScene(), dto.getSymbol()),
                "true", captchaVerifiedExpireTimeMinutes);
        System.out.println("验证码验证成功");
        System.out.println("scene = " + dto.getScene() + ", symbol = " + dto.getSymbol());
        System.out.println(RedisKeyConstants.buildGraphCaptchaVerifiedKey(dto.getScene(), dto.getSymbol()));

//        System.out.println("Captcha correct :" +  RedisKeyConstants.buildGraphCaptchaVerifiedKey(dto.getScene(), dto.getSymbol()));
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> deleteGraphCaptcha(String captchaId) {

        // "captcha:" + captchaId
        redisService.delete(RedisKeyConstants.buildGraphCaptchaKey(captchaId));

        return ResultResponse.success(null);
    }

    @Override
    public String generateSixDigitCaptcha(String scene, String symbol){
        // 生成六位数验证码
        String sixDigVerifyCode = VerificationUtil.generateSixNumVerifCode();
        // 存储到redis中
        redisService.setTemporarilyByMinutes(
                RedisKeyConstants.buildSixDigitCaptchaKey(scene, symbol),
                sixDigVerifyCode,
                sixDigVerifyCodeExpireTimeMinutes);
        return sixDigVerifyCode;
    }

}
