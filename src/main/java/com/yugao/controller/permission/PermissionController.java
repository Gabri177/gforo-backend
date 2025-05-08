package com.yugao.controller.permission;

import com.yugao.result.ResultFormat;
import com.yugao.service.business.permission.PermissionBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
