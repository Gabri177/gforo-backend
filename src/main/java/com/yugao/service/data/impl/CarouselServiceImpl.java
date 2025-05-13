package com.yugao.service.data.impl;

import com.yugao.domain.layout.Carousel;
import com.yugao.mapper.layout.CarouselMapper;
import com.yugao.service.data.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> getAllCarousels() {

        return carouselMapper.selectList(null);
    }

    @Override
    public void updateCarousel(Carousel carousel) {

        carouselMapper.updateById(carousel);
    }

    @Override
    public void addCarousel(Carousel carousel) {

        carouselMapper.insert(carousel);
    }

    @Override
    public void deleteCarousel(Long id) {

        carouselMapper.deleteById(id);
    }
}
