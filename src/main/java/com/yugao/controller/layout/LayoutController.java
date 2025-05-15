package com.yugao.controller.layout;

import com.yugao.dto.admin.AddCarouselDTO;
import com.yugao.dto.admin.UpdateCarouselDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.layout.LayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/layout")
@RequiredArgsConstructor
public class LayoutController {

    private final LayoutService layoutService;

    @GetMapping
    public ResponseEntity<ResultFormat> getCarousel(){

        return layoutService.getAllCarousels();
    }

    @PreAuthorize("principal.isSuperAdmin")
    @PostMapping("/carousel")
    public ResponseEntity<ResultFormat> updateCarousel(
            @Validated @RequestBody UpdateCarouselDTO updateCarouselDTO
            ){

        return layoutService.updateCarousel(updateCarouselDTO);
    }

    @PreAuthorize("principal.isSuperAdmin")
    @PutMapping("/carousel")
    public ResponseEntity<ResultFormat> addCarousel(
            @Validated @RequestBody AddCarouselDTO addCarouselDTO
    ){

        return layoutService.addCarousel(addCarouselDTO);
    }

    @PreAuthorize("principal.isSuperAdmin")
    @DeleteMapping("/carousel/{id}")
    public ResponseEntity<ResultFormat> deleteCarousel(
            @PathVariable(value = "id") Long id
    ){

        return layoutService.deleteCarousel(id);
    }
}
