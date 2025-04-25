package com.yugao.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yugao.domain.DiscussPost;
import com.yugao.domain.User;
import com.yugao.mapper.DiscussPostMapper;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.HomeService;
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
    private HomeService homeService;

    @GetMapping("/{index}")
    public ResponseEntity<ResultFormat> getIndexPage(
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "orderMode", defaultValue = "0") int orderMode,
            @PathVariable("index") Integer index) {

        return homeService.getIndexPage(index, limit, orderMode);
    }



}
