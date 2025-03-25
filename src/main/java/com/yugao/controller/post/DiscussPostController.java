package com.yugao.controller.post;

import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.data.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @GetMapping
    public ResponseEntity<ResultFormat> getDiscussPosts(
            @RequestParam(required = true) Long userId,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit
    ) {
        if(offset == null) {
            offset = 0;
        }
        if(limit == null) {
            limit = 10;
        }
        return ResultResponse.success(discussPostService.getDiscussPosts(userId, offset, limit, 0));
    }
}
