package com.yugao.controller.title;

import com.yugao.dto.title.AddTitleDTO;
import com.yugao.dto.title.UpdateTitleDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.title.TitleBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/title")
@RequiredArgsConstructor
public class TitleController {

    private final TitleBusinessService titleBusinessService;

    @PreAuthorize("hasAnyAuthority('title:list:own')")
    @GetMapping("/my")
    public ResponseEntity<ResultFormat> getMyTitles() {

        return titleBusinessService.getUserTitleList(null);
    }

    @PreAuthorize("hasAnyAuthority('title:update:own')")
    @PutMapping("/my/{titleId}")
    public ResponseEntity<ResultFormat> setMyTitle(
            @PathVariable Long titleId) {

        return titleBusinessService.setUserTitle(titleId);
    }

    @PreAuthorize("hasAnyAuthority('title:user-info:any')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResultFormat> getUserTitles(
            @PathVariable Long userId) {

        return titleBusinessService.getUserTitleList(userId);
    }


    @PreAuthorize("hasAnyAuthority('title:list:any')")
    @GetMapping("/list")
    public ResponseEntity<ResultFormat> getTitleListWithoutExp() {

        return titleBusinessService.getTitleListWithoutExp();
    }

    @PreAuthorize("hasAnyAuthority('title:update:any')")
    @PutMapping("/update")
    public ResponseEntity<ResultFormat> updateTitle(
            @Validated @RequestBody UpdateTitleDTO dto
    ) {

        return titleBusinessService.updateTitle(dto);
    }

    @PreAuthorize("hasAnyAuthority('title:delete:any')")
    @DeleteMapping("/{titleId}")
    public ResponseEntity<ResultFormat> deleteTitle(
            @PathVariable Long titleId) {

        return titleBusinessService.deleteTitle(titleId);
    }

    @PreAuthorize("hasAnyAuthority('title:grant:any')")
    @PutMapping("/grant/{userId}/{titleId}")
    public ResponseEntity<ResultFormat> grantTitle(
            @PathVariable Long userId,
            @PathVariable Long titleId) {

        return titleBusinessService.grantTitle(userId, titleId);
    }

    @PreAuthorize("hasAnyAuthority('title:add:any')")
    @PostMapping("/add")
    public ResponseEntity<ResultFormat> addTitle(
            @Validated @RequestBody AddTitleDTO dto
            ) {

        return titleBusinessService.addTitle(dto);
    }
}
