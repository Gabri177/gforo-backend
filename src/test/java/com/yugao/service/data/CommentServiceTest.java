package com.yugao.service.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void searchPostByIdTest(){
        Long postId = 1L;
        //System.out.println(commentService.findCommentById());
    }

    @Test
    public void getCountByBoardIdTest(){
        Long boardId = 0L;
        List<Long> postIds = discussPostService.getDiscussPostIdsByBoardId(boardId);
        System.out.println(commentService.getCommentCountByPostIds(postIds));
    }
}
