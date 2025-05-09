package com.yugao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.domain.post.DiscussPost;
import com.yugao.domain.user.User;
import com.yugao.mapper.post.DiscussPostMapper;
import com.yugao.service.data.DiscussPostService;
import com.yugao.service.data.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DiscussPostTest {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void TestGetAll() {
        DiscussPost discussPost = discussPostMapper.selectById(281);
        User user = userService.getUserById(101L);

        System.out.println("Discuss Post " + discussPost);
        System.out.println("User " + user);
    }

    @Test
    public void TestGetPage() {
        List<DiscussPost> records = discussPostService.getDiscussPosts(296L, 0L,1, 10, 0);
        System.out.println(records.size());
    }


    @Test
    public void TestGetPage2() {
        Page<DiscussPost> page = new Page<>(1, 5); // 查询第1页，每页1条数据
        IPage<DiscussPost> ipage = discussPostMapper.selectPage(page, new LambdaQueryWrapper<DiscussPost>()
                .ne(DiscussPost::getStatus, 2)
                .orderByDesc(DiscussPost::getType, DiscussPost::getCreateTime));
        List<DiscussPost> records = ipage.getRecords();
        System.out.println(records);

        // 打印分页信息
        System.out.println("总记录数：" + ipage.getTotal());
        System.out.println("总页数：" + ipage.getPages());
        System.out.println("当前页：" + ipage.getCurrent());
        System.out.println("每页记录数：" + ipage.getSize());
    }

}
