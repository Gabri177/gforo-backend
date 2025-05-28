package com.yugao.service.business.search.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.yugao.domain.post.DiscussPost;
import com.yugao.service.business.search.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private final ElasticsearchClient client;

    @Override
    public void savePost(DiscussPost post) {
        try {
            IndexRequest<DiscussPost> req = IndexRequest.of(i -> i
                    .index("post")
                    .id(post.getId().toString())
                    .document(post));
            client.index(req);
        } catch (IOException e) {
            log.error("[Elastic] Error saving post to Elasticsearch ", e);
        }
    }

    @Override
    public void deletePost(Long id) {
        try {
            DeleteRequest request = DeleteRequest.of(d -> d
                    .index("post")
                    .id(id.toString())
            );
            client.delete(request);
        } catch (IOException e) {
            log.error("[Elastic] Error deleting post from Elasticsearch ", e);
        }
    }

    @Override
    public List<DiscussPost> searchPost(String keyword, Integer currentPage, Integer pageSize) {

        List<DiscussPost> result = new ArrayList<>();
        try {
            Query query = Query.of(q -> q
                    .multiMatch(m -> m
                            .query(keyword)
                            .fields("title", "content")
                    ));

            Highlight highlight = Highlight.of(h -> h
                    .fields("title", HighlightField.of(f -> f))
                    .fields("content", HighlightField.of(f -> f))
                    .preTags("<em>")
                    .postTags("</em>")
            );

            SearchRequest request = SearchRequest.of(s -> s
                    .index("post")
                    .query(query)
                    .highlight(highlight)
                    .from((currentPage - 1) * pageSize)
                    .size(pageSize)
            );

            SearchResponse<DiscussPost> response = client.search(request, DiscussPost.class);

            for (Hit<DiscussPost> hit : response.hits().hits()) {
                DiscussPost post = hit.source();
                if (post == null) continue;

                Map<String, List<String>> highlights = hit.highlight();
                if (highlights != null) {
                    if (highlights.containsKey("title")) {
                        post.setTitle(highlights.get("title").get(0));
                    }
                    if (highlights.containsKey("content")) {
                        post.setContent(highlights.get("content").get(0));
                    }
                }

                result.add(post);
            }
        } catch (IOException e) {
            log.error("[Elastic] Error searching posts in Elasticsearch ", e);
        }

        return result;
    }

    @Override
    public long countPostByKeyword(String keyword) {
        try {
            Query query = Query.of(q -> q
                    .multiMatch(m -> m
                            .query(keyword)
                            .fields("title", "content")
                    ));

            CountRequest countRequest = CountRequest.of(c -> c
                    .index("post")
                    .query(query)
            );

            CountResponse response = client.count(countRequest);
            return response.count();

        } catch (IOException e) {
            log.error("[Elastic] Error counting posts in Elasticsearch ", e);
            return 0;
        }
    }

}
