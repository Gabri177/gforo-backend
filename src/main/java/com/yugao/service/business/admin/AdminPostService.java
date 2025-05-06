package com.yugao.service.business.admin;

import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface AdminPostService {


    ResponseEntity<ResultFormat> deletePost(Long postId);
}
