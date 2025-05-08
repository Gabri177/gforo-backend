package com.yugao.service.business.permission;

import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PermissionBusinessService {

    List<String> getPermissionCodesByUserId(Long userId);

    Boolean setBoardPoster(Long userId, Long boardId);

    Boolean deleteBoardPoster(Long userId, Long boardId);

    Boolean updatePermissionsToRole(Long roleId, List<Long> permissionIds);

    ResponseEntity<ResultFormat> getRoleDetailList(Long roleId);

}
