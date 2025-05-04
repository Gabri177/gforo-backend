package com.yugao.service.business.admin;

import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface AdminUserService {

    ResponseEntity<ResultFormat> logout(Long userId);
}
