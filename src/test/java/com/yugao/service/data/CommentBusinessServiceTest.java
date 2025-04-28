package com.yugao.service.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentBusinessServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void testGetCommentCount() {
        Long postId = 1L; // Replace with a valid postId
        Long count = commentService.getCommentCountByUserId(postId);
        System.out.println("Comment count for postId " + postId + ": " + count);
    }

    @Test
    public void testGetCommentByPostId(){
        Long postId = 30452L;
        Long count = commentService.getCommentCountByPostId(postId);
        System.out.println("Comment for postId: " + count);
    }
}
