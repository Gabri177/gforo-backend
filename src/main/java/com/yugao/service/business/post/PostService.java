package com.yugao.service.business.post;

import com.yugao.dto.comment.CommonContentDTO;
import com.yugao.dto.post.NewDiscussPostDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface PostService {

    public ResponseEntity<ResultFormat> getPostDetail(Long postId, Long currentPage);

    public ResponseEntity<ResultFormat> publishPost(NewDiscussPostDTO newDiscussPostDTO);

    public ResponseEntity<ResultFormat> deletePost(Long postId);

    public ResponseEntity<ResultFormat> updatePost(CommonContentDTO commonContentDTO);
}
