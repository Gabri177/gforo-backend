package com.yugao.service.business.post;

import com.yugao.dto.comment.CommonContentDTO;
import com.yugao.dto.post.NewDiscussPostDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface PostService {

    ResponseEntity<ResultFormat> getPostDetail(Long postId, Long currentPage, Integer pageSize, Boolean isAsc);

    ResponseEntity<ResultFormat> publishPost(NewDiscussPostDTO newDiscussPostDTO);

    ResponseEntity<ResultFormat> deletePost(Long postId);

    ResponseEntity<ResultFormat> updatePost(CommonContentDTO commonContentDTO);

    ResponseEntity<ResultFormat> getPosts(Long userId, Long boardId, Integer currentPage, Integer pageSize, Integer orderMode);

    ResponseEntity<ResultFormat> changePostType(Long postId, Integer type);

    ResponseEntity<ResultFormat> searchPost(String keyword, Integer currentPage, Integer pageSize);
}
