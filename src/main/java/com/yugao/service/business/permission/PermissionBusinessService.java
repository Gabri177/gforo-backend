package com.yugao.service.business.permission;
import com.yugao.dto.permission.AddNewRoleDTO;
import com.yugao.dto.permission.UpdateRolePermissionDTO;
import com.yugao.dto.permission.UpdateUserRoleDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface PermissionBusinessService {

    ResponseEntity<ResultFormat> getRoleDetailList();

    ResponseEntity<ResultFormat> updateUserRole(UpdateUserRoleDTO updateUserRoleDTO);

    ResponseEntity<ResultFormat> updateRolePermission(UpdateRolePermissionDTO updateRolePermissionDTO);

    ResponseEntity<ResultFormat> addNewRole(AddNewRoleDTO addNewRoleDTO);
}
