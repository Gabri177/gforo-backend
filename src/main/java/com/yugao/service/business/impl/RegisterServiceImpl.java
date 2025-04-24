package com.yugao.service.business.impl;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.converter.UserConverter;
import com.yugao.domain.User;
import com.yugao.dto.UserRegisterDTO;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.base.RedisService;
import com.yugao.service.business.RegisterService;
import com.yugao.service.data.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    public ResponseEntity<ResultFormat> registerAccount(UserRegisterDTO userRegisterDTO) {
        // 检查验证码是否正确
//        String redisKey = RedisKeyConstants.captchaVerified(RedisKeyConstants.REGISTER, userRegisterDTO.getUsername());
//        String redisCaptchaStatus = redisService.get(redisKey);
//        if (redisCaptchaStatus == null || !redisCaptchaStatus.equalsIgnoreCase("true")) {
//            return ResultResponse.error(ResultCode.BUSINESS_EXCEPTION,"Illegal registration");
//        }

        boolean res = redisService.verifyVerifiedCaptcha(RedisKeyConstants.REGISTER, userRegisterDTO.getUsername());
        if (!res) {
            return ResultResponse.error(ResultCode.NO_PASS_THE_CAPTCHA);
        }
//        redisService.delete(redisKey);
        redisService.deleteCaptcha(RedisKeyConstants.REGISTER);
        // 直接加入数据库 但是账户是没有验证的状态
        User userDomain = UserConverter.toDomain(userRegisterDTO);

        userService.addUser(userDomain);

        return ResultResponse.success("Register success");
    }
}
