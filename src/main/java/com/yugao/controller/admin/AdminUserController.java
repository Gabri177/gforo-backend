package com.yugao.controller.admin;

import com.yugao.dto.admin.ForceChangePasswordDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.admin.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    // 用户管理
    // 1. 用户列表
    // 2. 用户详情
    // 3. 用户禁用
    // 4. 用户启用
    // 5. 用户删除
    // 6. 用户修改密码
    // 9. 用户修改角色

    @Autowired
    private AdminUserService adminUserService;

    @PreAuthorize("hasAnyAuthority('user:info:any')")
    @GetMapping("/info/{userId}")
    public ResponseEntity<ResultFormat> getUserInfo(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1", name = "currentPage", required = false) Integer currentPage,
            @RequestParam(defaultValue = "10", name = "pageSize", required = false) Integer pageSize,
            @RequestParam(defaultValue = "true", name = "isAsc", required = false) Boolean isAsc
    ) {
        return adminUserService.getUserInfo(userId, currentPage, pageSize, isAsc);
    }

    @PreAuthorize("hasAnyAuthority('user:password:change:any')")
    @PutMapping("/change-password/{userId}")
    public ResponseEntity<ResultFormat> changePassword(
            @PathVariable("userId") Long userId,
            @Validated @RequestBody ForceChangePasswordDTO forceChangePasswordDTO
            ) {
        return adminUserService.changePassword(userId, forceChangePasswordDTO);
    }

    @PreAuthorize("hasAnyAuthority('user:activate:any')")
    @PutMapping("/activate/{userId}")
    public ResponseEntity<ResultFormat> activateUser(
            @PathVariable("userId") Long userId
    ) {
        return adminUserService.activateUser(userId);
    }

    @PreAuthorize("hasAnyAuthority('user:disable:any')")
    @PutMapping("/disable/{userId}")
    public ResponseEntity<ResultFormat> disableUser(
            @PathVariable("userId") Long userId
    ) {
        return adminUserService.disableUser(userId);
    }

    @PreAuthorize("hasAnyAuthority('user:delete:any')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResultFormat> deleteUser(
            @PathVariable("userId") Long userId
    ) {
        return adminUserService.deleteUser(userId);
    }

    @PreAuthorize("hasAnyAuthority('user:logout:any')")
    @DeleteMapping("/logout/{userId}")
    public ResponseEntity<ResultFormat> logout(
            @PathVariable("userId") Long userId
    ) {
        return adminUserService.logout(userId);
    }


}
