package com.yugao.service.business;

import com.yugao.dto.*;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface UserBusinessService {

    ResponseEntity<ResultFormat> getUserInfo(String token);

    ResponseEntity<ResultFormat> changePassword(UserChangePasswordDTO userChangePasswordDTO);

    ResponseEntity<ResultFormat> updateUserInfo(UserInfoUpdateDTO userInfoUpdateDTO);

    ResponseEntity<ResultFormat> sendVerifyEmail(UserVerifyEmailDTO userVerifyEmailDTO);

    ResponseEntity<ResultFormat> verifyEmail(String userId, String token);
}
