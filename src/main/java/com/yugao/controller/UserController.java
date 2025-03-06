package com.yugao.controller;

import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ResultFormat> getUserById(@PathVariable int userId) {
        return ResultResponse.success(userService.getUserById(userId), "Success");
    }
}
