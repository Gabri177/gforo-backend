package com.yugao.controller.auth;

import com.yugao.dto.auth.ActiveAccountDTO;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.auth.RegisterService;
import com.yugao.validation.ValidationGroups;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    /**
     * 注册用户
     * @param userRegisterDTO
     * @return ResultFormat
     */
    @PostMapping
    public ResponseEntity<ResultFormat> registerAccount(
            @Validated({ValidationGroups.Register.class}) @RequestBody UserRegisterDTO userRegisterDTO) {
        return registerService.registerAccount(userRegisterDTO);
    }

    @PostMapping("/activate")
    public ResponseEntity<ResultFormat> activateAccount(
            @Validated @RequestBody ActiveAccountDTO activeAccountDTO
    ) {
        return registerService.activateAccount(activeAccountDTO);
    }


}
