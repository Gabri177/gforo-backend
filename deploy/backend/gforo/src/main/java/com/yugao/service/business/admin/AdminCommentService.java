package com.yugao.service.business.admin;

import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface AdminCommentService {

    ResponseEntity<ResultFormat> getCommentList(Long boardId, Long currentPage, Integer pageSize, Boolean isAsc);

    ResponseEntity<ResultFormat> deleteComment(Long commentId);
}
