package com.yugao.converter;

import com.yugao.domain.layout.Carousel;
import com.yugao.dto.admin.AddCarouselDTO;
import com.yugao.dto.admin.UpdateCarouselDTO;

public class CarouselConverter {

    public static Carousel toCarousel(UpdateCarouselDTO dto){
        Carousel carousel = new Carousel();
        carousel.setId(dto.getId());
        carousel.setImageUrl(dto.getImageUrl());
        carousel.setTitle(dto.getTitle());
        carousel.setDescription(dto.getDescription());
        carousel.setTargetUrl(dto.getTargetUrl());
        return carousel;
    }

    public static Carousel toCarousel(AddCarouselDTO dto) {
        Carousel carousel = new Carousel();
        carousel.setImageUrl(dto.getImageUrl());
        carousel.setTitle(dto.getTitle());
        carousel.setDescription(dto.getDescription());
        carousel.setTargetUrl(dto.getTargetUrl());
        return carousel;
    }
}
