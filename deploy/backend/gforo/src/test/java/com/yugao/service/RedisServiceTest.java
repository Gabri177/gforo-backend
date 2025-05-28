package com.yugao.service;

import com.yugao.service.base.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void test() {
//        redisService.set("test", "test");
//        System.out.println(redisService.get("test"));
    }

}
