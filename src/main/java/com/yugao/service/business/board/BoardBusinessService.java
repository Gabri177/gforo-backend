package com.yugao.service.business.board;

import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface BoardBusinessService {

    ResponseEntity<ResultFormat> getBoardInfoList();
}
