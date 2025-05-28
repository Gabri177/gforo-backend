package com.yugao.service.data;

import com.yugao.service.data.post.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DicussPostServiceTest {

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void getLatestDiscussPostByBoardIdTest() {
        Long boardId = 0L;
        System.out.println(discussPostService.getLatestDiscussPostByBoardId(boardId));
    }
}
