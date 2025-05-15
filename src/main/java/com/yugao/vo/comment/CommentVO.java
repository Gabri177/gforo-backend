package com.yugao.vo.comment;

import com.yugao.vo.user.SimpleUserVO;
import lombok.Data;

import java.util.Date;

@Data
public class CommentVO {

    private Long id;
    private String content;
    private Date createTime;
    private Integer likeCount;
    private Boolean isLike; // 是否点赞
    private SimpleUserVO targetUserInfo; // 评论的目标用户信息
    private SimpleUserVO author;
    private Long postId; // 评论的帖子ID
}
