package com.yugao.vo.user;

import com.yugao.vo.comment.CommentVO;
import com.yugao.vo.post.SimpleDiscussPostVO;
import lombok.Data;

@Data
public class UserCommentsItemVO {

    private SimpleDiscussPostVO postInfo;

    private CommentVO commentInfo;

}
