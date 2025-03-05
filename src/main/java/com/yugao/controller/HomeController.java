package com.yugao.controller;

import com.yugao.domain.DiscussPost;
import com.yugao.domain.Page;
import com.yugao.domain.User;
import com.yugao.result.ResultFormat;
import com.yugao.service.DiscussPostService;
import com.yugao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @GetMapping("/{index}")
    public ResultFormat getIndexPage(
            Page page,
            @RequestParam(defaultValue = "0") int orderMode,
            @PathVariable("index") Integer index) {

        System.out.println(page);
        // 总帖子数量，用于分页
        int totalRows = discussPostService.getDiscussPostRows(0);
        page.setRows(totalRows);
        page.setCurrent(index);

        // 分页查询帖子
        List<DiscussPost> list = discussPostService.getDiscussPosts(
                0, page.getOffset(), page.getLimit(), orderMode);

        // 封装帖子+作者+点赞数
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);

                User user = userService.getUserById(post.getUserId());
                map.put("user", user);

//                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
//                map.put("likeCount", likeCount);

                discussPosts.add(map);
            }
        }
        // 封装分页信息和数据
        Map<String, Object> result = new HashMap<>();
        result.put("totalRows", totalRows);
        result.put("current", page.getCurrent());
        result.put("limit", page.getLimit());
        result.put("discussPosts", discussPosts);

        return ResultFormat.success(result);
    }



}
