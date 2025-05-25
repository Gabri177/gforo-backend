package com.yugao.controller.title;

import com.yugao.dto.title.AddTitleDTO;
import com.yugao.dto.title.UpdateTitleDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.title.TitleBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/title")
@RequiredArgsConstructor
public class TitleController {

    // TODO: 权限控制

    private final TitleBusinessService titleBusinessService;

    @GetMapping("/my")
    public ResponseEntity<ResultFormat> getMyTitles() {

        return titleBusinessService.getUserTitleList(null);
    }

    @PutMapping("/my/{titleId}")
    public ResponseEntity<ResultFormat> setMyTitle(
            @PathVariable Long titleId) {

        return titleBusinessService.setUserTitle(titleId);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<ResultFormat> getUserTitles(
            @PathVariable Long userId) {

        return titleBusinessService.getUserTitleList(userId);
    }



    @GetMapping("/list")
    public ResponseEntity<ResultFormat> getTitleListWithoutExp() {

        return titleBusinessService.getTitleListWithoutExp();
    }

    @PutMapping("/update")
    public ResponseEntity<ResultFormat> updateTitle(
            @Validated @RequestBody UpdateTitleDTO dto
    ) {

        return titleBusinessService.updateTitle(dto);
    }

    @DeleteMapping("/{titleId}")
    public ResponseEntity<ResultFormat> deleteTitle(
            @PathVariable Long titleId) {

        return titleBusinessService.deleteTitle(titleId);
    }

    @PutMapping("/grant/{userId}/{titleId}")
    public ResponseEntity<ResultFormat> grantTitle(
            @PathVariable Long userId,
            @PathVariable Long titleId) {

        return titleBusinessService.grantTitle(userId, titleId);
    }

    @PostMapping("/add")
    public ResponseEntity<ResultFormat> addTitle(
            @Validated @RequestBody AddTitleDTO dto
            ) {

        return titleBusinessService.addTitle(dto);
    }
}
