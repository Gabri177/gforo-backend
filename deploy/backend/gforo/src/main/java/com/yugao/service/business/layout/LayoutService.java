package com.yugao.service.business.layout;

import com.yugao.dto.admin.AddCarouselDTO;
import com.yugao.dto.admin.UpdateCarouselDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface LayoutService {

    ResponseEntity<ResultFormat> getAllCarousels();

    ResponseEntity<ResultFormat> updateCarousel(UpdateCarouselDTO updateCarouselDTO);

    ResponseEntity<ResultFormat> addCarousel(AddCarouselDTO addCarouselDTO);

    ResponseEntity<ResultFormat> deleteCarousel(Long id);
}
