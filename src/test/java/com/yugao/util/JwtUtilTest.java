package com.yugao.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void jwtUtilTest() {
        String newToken = jwtUtil.generateAccessToken("1234");
        System.out.println(newToken);
        String username = jwtUtil.getUserIdWithToken(newToken);
        System.out.println(username);
        System.out.println(jwtUtil.validateToken(newToken));
    }
}
