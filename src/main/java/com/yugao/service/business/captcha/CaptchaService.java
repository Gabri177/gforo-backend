package com.yugao.service.business.captcha;

import com.yugao.dto.captcha.GraphCaptchaDTO;
import org.springframework.http.ResponseEntity;
import com.yugao.result.ResultFormat;

public interface CaptchaService {

    ResponseEntity<ResultFormat> generateGraphCaptcha();

    ResponseEntity<ResultFormat> verifyGraphCaptcha(GraphCaptchaDTO graphCaptchaDTO);

    ResponseEntity<ResultFormat> deleteGraphCaptcha(String captchaId);

    String generateSixDigitCaptcha(String scene, String symbol);
}
