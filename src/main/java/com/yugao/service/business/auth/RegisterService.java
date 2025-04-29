package com.yugao.service.business.auth;

import com.yugao.dto.auth.ActiveAccountDTO;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface RegisterService {

    ResponseEntity<ResultFormat> registerAccount(UserRegisterDTO userRegisterDTO);

    ResponseEntity<ResultFormat> activateAccount(ActiveAccountDTO activeAccountDTO);
}
