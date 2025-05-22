package com.yugao.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yugao.domain.post.DiscussPost;
import com.yugao.enums.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class ElasticSearchTest {

    @Autowired
    private ElasticsearchClient client;

    @Test
    public void testInsert() throws IOException {
        DiscussPost post = new DiscussPost();
        post.setId(1L);
        post.setTitle("test");
        post.setContent("test content");
        post.setType(0);
        post.setStatus(StatusEnum.NORMAL);
        post.setCreateTime(new Date());
        post.setUserId(1L);

        IndexResponse res = client.index(i -> i
                .index("post")
                .id(post.getId().toString())
                .document(post)
        );
        System.out.println("index result: " + res.result());
    }

    @Test
    public void getTest() throws IOException {
        GetResponse<DiscussPost> response = client.get(g -> g
                .index("post")
                .id("1"), DiscussPost.class);

        if (response.found()) {
            DiscussPost article = response.source();
            System.out.println("post: " + article);
        } else {
            System.out.println("post not found");
        }
    }

    @Test
    public void searchTest() throws IOException {
        SearchResponse<DiscussPost> response = client.search(s -> s
                        .index("post")
                        .query(q -> q
                                .match(m -> m
                                        .field("title")
                                        .query("test")
                                )
                        ),
                DiscussPost.class);

        List<Hit<DiscussPost>> hits = response.hits().hits();
        for (Hit<DiscussPost> hit : hits) {
            System.out.println("found: " + hit.source());
        }
    }


}
