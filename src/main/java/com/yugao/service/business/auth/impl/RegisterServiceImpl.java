package com.yugao.service.business.auth.impl;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.converter.UserConverter;
import com.yugao.domain.User;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.base.RedisService;
import com.yugao.service.business.auth.RegisterService;
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

        boolean res = redisService.verifyVerifiedCaptcha(RedisKeyConstants.REGISTER, userRegisterDTO.getUsername());
        if (!res) {
            return ResultResponse.error(ResultCode.NO_PASS_THE_CAPTCHA);
        }
        redisService.deleteCaptcha(RedisKeyConstants.REGISTER);
        // 直接加入数据库 但是账户是没有验证的状态
        User userDomain = UserConverter.toDomain(userRegisterDTO);
        userService.addUser(userDomain);
        return ResultResponse.success(null);
    }
}
