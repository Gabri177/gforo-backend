package com.yugao.service.handler;

import com.yugao.service.data.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class PostHandlerTest {

    @Autowired
    private CommentService commentService;


    @Test
    public void testGetCommentCount() {
        Long postId = 109L; // Replace with a valid post ID
        Long count = commentService.getCommentCountByUserId(postId);
        System.out.println("Comment count for postId " + postId + ": " + count);
    }
}
