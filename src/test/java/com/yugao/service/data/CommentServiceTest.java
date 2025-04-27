package com.yugao.service.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Test
    public void searchPostByIdTest(){
        Long postId = 1L;
        //System.out.println(commentService.findCommentById());
    }
}
