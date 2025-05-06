package com.yugao.service.builder;

import com.yugao.converter.UserConverter;
import com.yugao.domain.Board;
import com.yugao.domain.DiscussPost;
import com.yugao.domain.User;
import com.yugao.service.data.CommentService;
import com.yugao.service.data.DiscussPostService;
import com.yugao.service.data.UserService;
import com.yugao.vo.board.BoardInfosItemVO;
import com.yugao.vo.post.CurrentPageItemVO;
import com.yugao.vo.post.SimpleDiscussPostVO;
import com.yugao.vo.user.SimpleUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemBuilder {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

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
        if (discussPost != null){
            CurrentPageItemVO currentPageItemVO = buildCurrentPageItemVO(discussPost);
            boardInfosItemVO.setLatestPost(currentPageItemVO);
        }

        return boardInfosItemVO;
    }

    public CurrentPageItemVO buildCurrentPageItemVO(DiscussPost post) {

        CurrentPageItemVO currentPageItemVO = new CurrentPageItemVO();
        SimpleDiscussPostVO simpleDiscussPostVO = new SimpleDiscussPostVO();
        BeanUtils.copyProperties(post, simpleDiscussPostVO);
        currentPageItemVO.setDiscussPosts(simpleDiscussPostVO);

        User user = userService.getUserById(post.getUserId());
        SimpleUserVO userInfoVO = UserConverter.toSimpleVO(user);
        currentPageItemVO.setUser(userInfoVO);
        //System.out.println("用户简单信息: " + userInfoVO);

        Long postCommentCount = commentService.getCommentCountByPostId(post.getId());
        System.out.println("用户状态: " + user.getStatus().getValue() + " username: " + user.getUsername());
        //System.out.println("查找 (" + post.getTitle() + ") postId为: " + post.getId() + " 的评论数量: " + postCommentCount);
        currentPageItemVO.setCommentCount(postCommentCount);

//                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
//                map.put("likeCount", likeCount);
        // 还没有封装点赞数量

        return currentPageItemVO;
    }
}
