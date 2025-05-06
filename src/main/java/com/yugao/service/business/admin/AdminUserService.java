package com.yugao.service.business.admin;

import com.yugao.dto.admin.ForceChangePasswordDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface AdminUserService {

    ResponseEntity<ResultFormat> getUserInfo(Long userId, Integer currentPage, Integer pageSize, Boolean isAsc);

    ResponseEntity<ResultFormat> changePassword(Long userId, ForceChangePasswordDTO forceChangePasswordDTO);

    ResponseEntity<ResultFormat> activateUser(Long userId);

    ResponseEntity<ResultFormat> disableUser(Long userId);

    ResponseEntity<ResultFormat> deleteUser(Long userId);

    ResponseEntity<ResultFormat> logout(Long userId);
}
