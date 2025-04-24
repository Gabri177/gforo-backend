package com.yugao.service.business;

import com.yugao.dto.UserForgetPasswordDTO;
import com.yugao.dto.UserForgetPasswordResetDTO;
import com.yugao.dto.UserRegisterDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ResultFormat> login(UserRegisterDTO userRegisterDTO);
    ResponseEntity<ResultFormat> logout(String accessToken);
    ResponseEntity<ResultFormat> refresh(String refreshToken);
    ResponseEntity<ResultFormat> sendForgetPasswordCode(UserForgetPasswordDTO userForgetPasswordDTO);
    ResponseEntity<ResultFormat> verifyForgetPasswordCode(UserForgetPasswordDTO userForgetPasswordDTO, String code);
    ResponseEntity<ResultFormat> resetPassword(UserForgetPasswordResetDTO userForgetPasswordResetDTO);
}
