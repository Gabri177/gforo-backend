package com.yugao.service.business.captcha;

import com.yugao.dto.captcha.GraphCaptchaDTO;
import org.springframework.http.ResponseEntity;
import com.yugao.result.ResultFormat;

public interface CaptchaService {

    ResponseEntity<ResultFormat> generateCaptcha();

    ResponseEntity<ResultFormat> verifyCaptcha(GraphCaptchaDTO graphCaptchaDTO);

    ResponseEntity<ResultFormat> deleteCaptcha(String captchaId);
}
