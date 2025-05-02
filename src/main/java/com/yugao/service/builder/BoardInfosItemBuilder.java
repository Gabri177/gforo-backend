package com.yugao.service.builder;

import com.yugao.converter.PostConverter;
import com.yugao.domain.Board;
import com.yugao.domain.Comment;
import com.yugao.domain.DiscussPost;
import com.yugao.service.data.CommentService;
import com.yugao.service.data.DiscussPostService;
import com.yugao.vo.board.BoardInfosItemVO;
import com.yugao.vo.post.SimpleDiscussPostVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardInfosItemBuilder {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    public BoardInfosItemVO buildBoardInfosItemVO(Board board) {

        BoardInfosItemVO boardInfosItemVO = new BoardInfosItemVO();
        BeanUtils.copyProperties(board, boardInfosItemVO);
        boardInfosItemVO.setPostCount(discussPostService.getDiscussPostRows(0L, board.getId()));
        List<Long> postIds = discussPostService.getDiscussPostIdsByBoardId(board.getId());
        System.out.println("postIds: " + postIds);
        Long commentCount = commentService.getCommentCountByPostIds(postIds);
        System.out.println("commentCount: " + commentCount);
        boardInfosItemVO.setCommentCount(commentCount);
        DiscussPost discussPost = discussPostService.getLatestDiscussPostByBoardId(board.getId());
        SimpleDiscussPostVO simpleDiscussPost = new SimpleDiscussPostVO();
        if (discussPost != null)
            BeanUtils.copyProperties(discussPost, simpleDiscussPost);
        boardInfosItemVO.setLatestPost(simpleDiscussPost);
        return boardInfosItemVO;
    }
}
