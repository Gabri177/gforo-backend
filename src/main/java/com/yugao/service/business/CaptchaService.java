package com.yugao.service.business;

import org.springframework.http.ResponseEntity;
import com.yugao.result.ResultFormat;

public interface CaptchaService {
    ResponseEntity<ResultFormat> generateCaptcha();
    ResponseEntity<ResultFormat> verifyCaptcha(String verCode, String captchaId, String username, String scene);
    ResponseEntity<ResultFormat> deleteCaptcha(String captchaId);
}
