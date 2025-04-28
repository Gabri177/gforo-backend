package com.yugao.service.business;

import com.yugao.dto.CommonContentDTO;
import com.yugao.dto.NewDiscussPostDTO;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import org.springframework.http.ResponseEntity;

public interface PostService {

    public ResponseEntity<ResultFormat> getPostDetail(Long postId, Long currentPage);

    public ResponseEntity<ResultFormat> publishPost(NewDiscussPostDTO newDiscussPostDTO);

    public ResponseEntity<ResultFormat> deletePost(Long postId);

    public ResponseEntity<ResultFormat> updatePost(CommonContentDTO commonContentDTO);
}
