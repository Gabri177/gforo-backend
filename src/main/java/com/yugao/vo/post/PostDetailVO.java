package com.yugao.vo.post;

import com.yugao.vo.comment.CommentVO;
import com.yugao.vo.user.SimpleUserVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostDetailVO {

    private Long id;
    private String title;
    private String content;
    private Date createTime;
    private Integer likeCount;
    private Boolean isLike;
    private SimpleUserVO author; // 作者（简略版用户信息）
    private List<CommentVO> replies; // 回复列表
    private Integer type; // 帖子类型，0-普通，1-置顶 2-精华
}
