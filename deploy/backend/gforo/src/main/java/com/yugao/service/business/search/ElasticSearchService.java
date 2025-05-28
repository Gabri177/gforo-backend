package com.yugao.service.business.search;

import com.yugao.domain.post.DiscussPost;

import java.util.List;

public interface ElasticSearchService {

    void savePost(DiscussPost post);
    void deletePost(Long id);
    List<DiscussPost> searchPost(String keyword, Integer currentPage, Integer pageSize);
    long countPostByKeyword(String keyword);
}
