package com.yugao.controller;

import com.yugao.domain.DiscussPost;
import com.yugao.domain.User;
import com.yugao.mapper.DiscussPostMapper;
import com.yugao.mapper.UserMapper;
import com.yugao.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DiscussPostTest {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserService userService;

    @Test
    public void TestGetAll() {
        DiscussPost discussPost = discussPostMapper.selectById(281);
        User user = userService.getUserById(101);

        System.out.println("Discuss Post " + discussPost);
        System.out.println("User " + user);
    }
}
