package com.yugao.vo.board;

import com.yugao.vo.post.SimpleDiscussPostVO;
import lombok.Data;

@Data
public class BoardInfosItemVO {

    private Long id;

    private String name;

    private String description;

    private String iconUrl;

    private Long postCount; // 帖子总数

    private Long commentCount;

    private SimpleDiscussPostVO latestPost; // 最新帖子
}
