package com.yugao.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTokenServiceTest {

    @Autowired
    private UserTokenService userTokenService;

    @Test
    public void userTokenDeleteTest() {
        System.out.println("delete: " + userTokenService.deleteUserTokenByUserId(232L));
    }
}
