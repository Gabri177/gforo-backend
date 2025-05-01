package com.yugao.controller.captcha;

import com.yugao.dto.captcha.GraphCaptchaDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.captcha.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    /**
     * 获取验证码
     * @return
     */
    @GetMapping()
    public ResponseEntity<ResultFormat> getCaptchaInfo(){
       return captchaService.generateGraphCaptcha();
    }

    @PostMapping
    public ResponseEntity<ResultFormat> checkCaptcha(
            @Validated @RequestBody GraphCaptchaDTO graphCaptchaDTO
            ) {
        return captchaService.verifyGraphCaptcha(graphCaptchaDTO);
    }

    /**
     * 切换验证码需要对之前存储的验证码进行删除
     * 节省存储空间
     * @return
     */
    @DeleteMapping("/{captchaId}")
    public ResponseEntity<ResultFormat> logout(@PathVariable String captchaId) {
        return captchaService.deleteGraphCaptcha(captchaId);
    }
}
