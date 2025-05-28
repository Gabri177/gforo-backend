package com.yugao.service.business.layout.impl;

import com.yugao.converter.CarouselConverter;
import com.yugao.dto.admin.AddCarouselDTO;
import com.yugao.dto.admin.UpdateCarouselDTO;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.layout.LayoutService;
import com.yugao.service.data.layout.CarouselService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LayoutServiceImpl implements LayoutService {

    private final CarouselService carouselService;

    @Override
    public ResponseEntity<ResultFormat> getAllCarousels() {

        return ResultResponse.success(carouselService.getAllCarousels());
    }

    @Override
    public ResponseEntity<ResultFormat> updateCarousel(UpdateCarouselDTO updateCarouselDTO) {

        carouselService.updateCarousel(CarouselConverter.toCarousel(updateCarouselDTO));
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> addCarousel(AddCarouselDTO addCarouselDTO) {

        carouselService.addCarousel(CarouselConverter.toCarousel(addCarouselDTO));
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> deleteCarousel(Long id) {

        carouselService.deleteCarousel(id);
        return ResultResponse.success(null);
    }
}
