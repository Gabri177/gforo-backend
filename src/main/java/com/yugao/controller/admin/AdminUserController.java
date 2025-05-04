package com.yugao.controller.admin;

import com.yugao.dto.user.UserChangePasswordDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.admin.AdminUserService;
import com.yugao.service.data.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // 当boardId为0时，表示查询所有的用户
    @GetMapping("/list/{boardId}")
    public ResponseEntity<ResultFormat> getUserList(
            @PathVariable("boardId") Long boardId,
            @RequestParam(defaultValue = "1", name = "currentPage", required = false) Integer currentPage,
            @RequestParam(defaultValue = "10", name = "pageSize", required = false) Integer pageSize
    ) {
        return null;
    }

    @GetMapping("/info/{userId}")
    public ResponseEntity<ResultFormat> getUserInfo(
            @PathVariable("userId") Long userId
    ) {
        return null;
    }

    @PutMapping("/change-password")
    public ResponseEntity<ResultFormat> changePassword(
            @Validated @RequestBody UserChangePasswordDTO userChangePasswordDTO
            ) {
        return null;
    }

    @PutMapping("/activate/{userId}")
    public ResponseEntity<ResultFormat> activateUser(
            @PathVariable("userId") Long userId
    ) {
        return null;
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResultFormat> deleteUser(
            @PathVariable("userId") Long userId
    ) {
        return null;
    }

    @DeleteMapping("/logout/{userId}")
    public ResponseEntity<ResultFormat> logout(
            @PathVariable("userId") Long userId
    ) {
        return adminUserService.logout(userId);
    }


}
