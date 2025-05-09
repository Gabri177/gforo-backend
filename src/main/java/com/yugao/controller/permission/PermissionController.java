package com.yugao.controller.permission;

import com.yugao.dto.permission.AddNewRoleDTO;
import com.yugao.dto.permission.UpdateRolePermissionDTO;
import com.yugao.dto.permission.UpdateUserRoleDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.permission.PermissionBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionBusinessService permissionBusinessService;

    @GetMapping("/role-detail")
    public ResponseEntity<ResultFormat> getRoleDetailList(
            @RequestParam(name = "roleId", required = false, defaultValue = "0") Long roleId
    ){
        return permissionBusinessService.getRoleDetailList(roleId);
    }

    @PutMapping("/user-role")
    public ResponseEntity<ResultFormat> updateUserRole(
            @Validated @RequestBody UpdateUserRoleDTO updateUserRoleDTO
    ){
        return permissionBusinessService.updateUserRole(updateUserRoleDTO);
    }

    @PutMapping("/role-permission")
    public ResponseEntity<ResultFormat> updateRolePermission(
            @Validated @RequestBody UpdateRolePermissionDTO updateRolePermissionDTO
    ){
        return permissionBusinessService.updateRolePermission(updateRolePermissionDTO);
    }

    @PostMapping("/new-role")
    public ResponseEntity<ResultFormat> addNewRole(
            @Validated @RequestBody AddNewRoleDTO addNewRoleDTO
    ){
        return permissionBusinessService.addNewRole(addNewRoleDTO);
    }
}
