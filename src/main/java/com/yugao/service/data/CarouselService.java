package com.yugao.service.data;

import com.yugao.domain.layout.Carousel;

import java.util.List;

public interface CarouselService {

    List<Carousel> getAllCarousels();

    void updateCarousel(Carousel carousel);

    void addCarousel(Carousel carousel);

    void deleteCarousel(Long id);
}
