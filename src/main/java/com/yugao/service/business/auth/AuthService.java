package com.yugao.service.business.auth;

import com.yugao.dto.auth.RefreshTokenDTO;
import com.yugao.dto.auth.UserForgetPasswordDTO;
import com.yugao.dto.auth.UserForgetPasswordResetDTO;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ResultFormat> login(UserRegisterDTO userRegisterDTO);
    ResponseEntity<ResultFormat> logout(String accessToken);
    ResponseEntity<ResultFormat> refresh(RefreshTokenDTO refreshTokenDTO);
    ResponseEntity<ResultFormat> sendForgetPasswordCode(UserForgetPasswordDTO userForgetPasswordDTO);
    ResponseEntity<ResultFormat> verifyForgetPasswordCode(UserForgetPasswordDTO userForgetPasswordDTO, String code);
    ResponseEntity<ResultFormat> resetPassword(UserForgetPasswordResetDTO userForgetPasswordResetDTO);
}
