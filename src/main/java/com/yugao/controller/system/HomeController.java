package com.yugao.controller.system;

import com.yugao.result.ResultFormat;
import com.yugao.service.business.system.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
