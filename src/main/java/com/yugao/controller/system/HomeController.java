package com.yugao.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yugao.domain.DiscussPost;
import com.yugao.domain.User;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.data.DiscussPostService;
import com.yugao.service.data.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @GetMapping("/{index}")
    public ResponseEntity<ResultFormat> getIndexPage(
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "orderMode", defaultValue = "0") int orderMode,
            @PathVariable("index") Integer index) {

        // orderMode 0: 按照时间排序 1: 按照热度排序
        // 总帖子数量，用于分页
        Long totalRows = discussPostService.getDiscussPostRows(0L);

        // 分页查询帖子
        // userId = 0 表示查询所有用户的帖子
        IPage<DiscussPost> pages = discussPostService.getDiscussPosts(
                0L, index, limit, orderMode);
        List<DiscussPost> list = pages.getRecords();

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
        result.put("current", pages.getCurrent());
        result.put("limit", limit);
        result.put("discussPosts", discussPosts);

        return ResultResponse.success(result);
    }



}
