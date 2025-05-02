package com.yugao.util.security;

import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    private SecurityUtils() {}

    public static Long mustGetLoginUserId() {
        Long id = getLoginUserId();
        if (id == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_LOGIN);
        }
        return id;
    }

    public static Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            return loginUser.getId();
        }
        return null;
    }

    public static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            return loginUser;
        }
        return null;
    }

    public static LoginUser mustGetLoginUser(){
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_LOGIN);
        }
        return loginUser;
    }
}
