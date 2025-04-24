package com.yugao.service.business;

import com.yugao.dto.UserRegisterDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface RegisterService {
    ResponseEntity<ResultFormat> registerAccount(UserRegisterDTO userRegisterDTO);
}
