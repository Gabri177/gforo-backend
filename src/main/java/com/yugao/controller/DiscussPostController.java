package com.yugao.controller;

import com.yugao.result.ResultFormat;
import com.yugao.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @GetMapping
    public ResultFormat getDiscussPosts(
            @RequestParam(required = true) int userId,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit
    ) {
        if(offset == null) {
            offset = 0;
        }
        if(limit == null) {
            limit = 10;
        }
        return ResultFormat.success(discussPostService.getDiscussPosts(userId, offset, limit, 0));
    }
}
