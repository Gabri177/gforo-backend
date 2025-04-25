package com.yugao.service.business;

import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface HomeService {
    ResponseEntity<ResultFormat> getIndexPage(int index, int limit, int orderMode);
}
