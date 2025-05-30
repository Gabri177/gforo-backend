package com.yugao.service.business.user;

import com.yugao.dto.auth.ActiveAccountDTO;
import com.yugao.dto.auth.UserVerifyEmailDTO;
import com.yugao.dto.user.UserChangePasswordDTO;
import com.yugao.dto.user.UserChangeUsernameDTO;
import com.yugao.dto.user.UserInfoUpdateDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface UserBusinessService {

    ResponseEntity<ResultFormat> logout(String deviceId);

    ResponseEntity<ResultFormat> getUserInfo(Long userId);

    ResponseEntity<ResultFormat> changePassword(UserChangePasswordDTO userChangePasswordDTO);

    ResponseEntity<ResultFormat> updateUserInfo(UserInfoUpdateDTO userInfoUpdateDTO);

    ResponseEntity<ResultFormat> sendVerifyEmail(UserVerifyEmailDTO userVerifyEmailDTO);

    ResponseEntity<ResultFormat> verifyEmail(ActiveAccountDTO activeAccountDTO);

    ResponseEntity<ResultFormat> changeUsername(UserChangeUsernameDTO userChangeUsernameDTO);

    ResponseEntity<ResultFormat> getCommentsByUserId(
                                                     Integer currentPage,
                                                     Integer pageSize,
                                                     Boolean isAsc);

    ResponseEntity<ResultFormat> getPostsByUserId(
            Long userId,
            Integer currentPage,
            Integer pageSize,
            Boolean isAsc
    );



}
