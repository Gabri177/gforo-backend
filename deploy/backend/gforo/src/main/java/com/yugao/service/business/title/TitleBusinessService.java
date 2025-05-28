package com.yugao.service.business.title;

import com.yugao.dto.title.AddTitleDTO;
import com.yugao.dto.title.UpdateTitleDTO;
import com.yugao.enums.EntityTypeEnum;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface TitleBusinessService {
    void addExp(Long userId, int value, String reason, EntityTypeEnum relatedType, Long relatedId);
    void subtractExp(Long userId, int value, String reason, EntityTypeEnum relatedType, Long relatedId);
    void refreshUserExpTitle(Long userId);
    ResponseEntity<ResultFormat> getUserTitleList(Long userId);
    ResponseEntity<ResultFormat> getTitleListWithoutExp();
    ResponseEntity<ResultFormat> setUserTitle(Long titleId);

    ResponseEntity<ResultFormat> updateTitle(UpdateTitleDTO updateTitleDTO);
    ResponseEntity<ResultFormat> deleteTitle(Long titleId);
    ResponseEntity<ResultFormat> grantTitle(Long userId, Long titleId);
    ResponseEntity<ResultFormat> addTitle(AddTitleDTO addTitleDTO);
}
