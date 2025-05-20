package com.yugao.controller.permission;

import com.yugao.dto.permission.AddNewRoleDTO;
import com.yugao.dto.permission.UpdateRolePermissionDTO;
import com.yugao.dto.permission.UpdateUserRoleDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.permission.PermissionBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionBusinessService permissionBusinessService;

    //只显示role等级比当前用户最高身份低的role信息
    @GetMapping("/role-detail")
    public ResponseEntity<ResultFormat> getRoleDetailList(){
        return permissionBusinessService.getRoleDetailList();
    }


    // 给用户分配角色
    @PreAuthorize("principal.isSuperAdmin || principal.isAdmin")
    @PutMapping("/user-role")
    public ResponseEntity<ResultFormat> updateUserRole(
            @Validated @RequestBody UpdateUserRoleDTO updateUserRoleDTO
    ){
        return permissionBusinessService.updateUserRole(updateUserRoleDTO);
    }

    // 更新role和permission的关系
    @PreAuthorize("principal.isSuperAdmin || principal.isAdmin")
    @PutMapping("/role-permission")
    public ResponseEntity<ResultFormat> updateRolePermission(
            @Validated @RequestBody UpdateRolePermissionDTO updateRolePermissionDTO
    ){
        System.out.println("updateRolePermissionDTO = " + updateRolePermissionDTO);
        return permissionBusinessService.updateRolePermission(updateRolePermissionDTO);
    }

    // 添加新的角色并分配权限
    @PreAuthorize("principal.isSuperAdmin || principal.isAdmin")
    @PostMapping("/role-permission")
    public ResponseEntity<ResultFormat> addRolePermission(
            @Validated @RequestBody AddNewRoleDTO addNewRoleDTO
    ){
        return permissionBusinessService.addNewRole(addNewRoleDTO);
    }

    @PreAuthorize("principal.isSuperAdmin || principal.isAdmin")
    @DeleteMapping("/role/{id}")
    public ResponseEntity<ResultFormat> deleteRole(
            @PathVariable("id") Long id
    ){
        return permissionBusinessService.deleteRole(id);
    }
}
