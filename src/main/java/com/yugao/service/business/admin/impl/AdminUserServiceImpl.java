package com.yugao.service.business.admin.impl;

import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.admin.AdminUserService;
import com.yugao.service.data.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private UserTokenService userTokenService;


    @Override
    public ResponseEntity<ResultFormat> logout(Long userId) {

        Boolean result = userTokenService.deleteUserTokenByUserId(userId);
        if (!result)
            throw new BusinessException(ResultCodeEnum.LOGOUT_WITHOUT_LOGIN);
        return ResultResponse.success(null);
    }
}
