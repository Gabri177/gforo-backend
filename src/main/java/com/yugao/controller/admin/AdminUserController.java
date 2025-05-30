package com.yugao.controller.admin;

import com.yugao.dto.admin.ForceChangePasswordDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    // 用户管理
    // 1. 用户列表
    // 2. 用户详情
    // 3. 用户禁用
    // 4. 用户启用
    // 5. 用户删除
    // 6. 用户修改密码
    // 9. 用户修改角色

    private final AdminUserService adminUserService;

    // 只显示最高身份的用户比当前用户最高身份低的用户
    @PreAuthorize("hasAnyAuthority('user:info:any')")
    @GetMapping("/info")
    public ResponseEntity<ResultFormat> getUserInfo(
            @RequestParam(defaultValue = "1", name = "currentPage", required = false) Integer currentPage,
            @RequestParam(defaultValue = "10", name = "pageSize", required = false) Integer pageSize,
            @RequestParam(defaultValue = "true", name = "isAsc", required = false) Boolean isAsc
    ) {
        return adminUserService.getUserInfo(currentPage, pageSize, isAsc);
    }

    @GetMapping("/info/username-like")
    public ResponseEntity<?> getUserList(
            @RequestParam(required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "true") Boolean isAsc,
            @RequestParam(required = false) String username
    ) {
        System.out.println("getUserList");
        return adminUserService.getUserList(currentPage, pageSize, isAsc, username);
    }


    // 超级管理员可以更改任意用户的密码
    @PreAuthorize("hasAnyAuthority('user:password:change:any')")
    @PutMapping("/change-password/{userId}")
    public ResponseEntity<ResultFormat> changePassword(
            @PathVariable("userId") Long userId,
            @Validated @RequestBody ForceChangePasswordDTO forceChangePasswordDTO
            ) {
        return adminUserService.changePassword(userId, forceChangePasswordDTO);
    }


    // 启用任意用户的账号
    @PreAuthorize("hasAnyAuthority('user:activate:any')")
    @PutMapping("/activate/{userId}")
    public ResponseEntity<ResultFormat> activateUser(
            @PathVariable("userId") Long userId
    ) {
        return adminUserService.activateUser(userId);
    }

    // 禁用任意用户的账号
    @PreAuthorize("hasAnyAuthority('user:disable:any')")
    @PutMapping("/disable/{userId}")
    public ResponseEntity<ResultFormat> disableUser(
            @PathVariable("userId") Long userId
    ) {
        return adminUserService.disableUser(userId);
    }

    // 删除任意的用户 超级管理员的权利
    @PreAuthorize("hasAnyAuthority('user:delete:any')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResultFormat> deleteUser(
            @PathVariable("userId") Long userId
    ) {
        return adminUserService.deleteUser(userId);
    }

    // 登出任意的用户 超级管理员的权利
    @PreAuthorize("hasAnyAuthority('user:logout:any')")
    @DeleteMapping("/logout/{userId}")
    public ResponseEntity<ResultFormat> logout(
            @PathVariable("userId") Long userId
    ) {
        return adminUserService.logout(userId);
    }


}
