package com.yugao.controller.system;

import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/visit")
public class VisitController {

    @PostMapping("/add")
    public ResponseEntity<ResultFormat> addVisit() {
        return null;
    }

    @GetMapping("/today")
    public ResponseEntity<ResultFormat> getTodayVisitCount() {
        return null;
    }
}
