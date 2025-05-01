package com.yugao.service.business.captcha;

import org.springframework.http.ResponseEntity;
import com.yugao.result.ResultFormat;

public interface CaptchaService {
    ResponseEntity<ResultFormat> generateCaptcha();
    ResponseEntity<ResultFormat> verifyCaptcha(String verCode, String captchaId, String username, String scene);
    ResponseEntity<ResultFormat> deleteCaptcha(String captchaId);
    boolean verifyVerifiedCaptcha(String scene, String username);
    void deleteVerifiedCaptcha(String scene, String username);
}
