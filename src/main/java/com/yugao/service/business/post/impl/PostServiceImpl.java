package com.yugao.service.business.post.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yugao.converter.DiscussPostConverter;
import com.yugao.converter.UserConverter;
import com.yugao.domain.DiscussPost;
import com.yugao.domain.User;
import com.yugao.dto.comment.CommonContentDTO;
import com.yugao.dto.post.BoardPostsPageDTO;
import com.yugao.dto.post.NewDiscussPostDTO;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.post.PostService;
import com.yugao.service.data.BoardService;
import com.yugao.service.data.CommentService;
import com.yugao.service.data.DiscussPostService;
import com.yugao.service.data.UserService;
import com.yugao.service.handler.PostHandler;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.post.CurrentPageItemVO;
import com.yugao.vo.post.CurrentPageVO;
import com.yugao.vo.post.PostPageVO;
import com.yugao.vo.post.SimpleDiscussPostVO;
import com.yugao.vo.user.SimpleUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostHandler postHandler;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    @Override
    public ResponseEntity<ResultFormat> getPostDetail(Long postId, Long currentPage) {

        System.out.println("getPostDetail: " + postId + " " + currentPage);
        PostPageVO postPageVO = new PostPageVO();
        postPageVO.setOriginalPost(postHandler.getOriginalPostDetail(postId));
        postPageVO.setReplies(postHandler.getCommentPostDetailList(postId, currentPage));
        postPageVO.setCurrentPage(currentPage);
        postPageVO.setTotalRows(commentService.getCommentCountByPostId(postId));
        postPageVO.setLimit(10); // 暂时没用 以防万一

        return ResultResponse.success(postPageVO);
    }

    @Override
    public ResponseEntity<ResultFormat> publishPost(NewDiscussPostDTO newDiscussPostDTO) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        if (!boardService.isExistBoard(newDiscussPostDTO.getBoardId()))
            throw new BusinessException(ResultCode.BOARD_NOT_FOUND);
        DiscussPost newDiscussPost = DiscussPostConverter.newDiscussPostDTOtoDiscussPost(newDiscussPostDTO, userId);
        discussPostService.addDiscussPost(newDiscussPost);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> deletePost(Long postId) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        DiscussPost post = discussPostService.getDiscussPostById(postId);
        if (post == null)
            throw new BusinessException(ResultCode.POST_NOT_FOUND);
        if (!userId.equals(post.getUserId()))
            throw new BusinessException(ResultCode.USER_NOT_AUTHORIZED);
        discussPostService.deleteDiscussPost(postId);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> updatePost(CommonContentDTO commonContentDTO) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        DiscussPost post = discussPostService.getDiscussPostById(commonContentDTO.getId());
        if (post == null)
            throw new BusinessException(ResultCode.POST_NOT_FOUND);
        if (!userId.equals(post.getUserId()))
            throw new BusinessException(ResultCode.USER_NOT_AUTHORIZED);
        post.setTitle(commonContentDTO.getTitle());
        post.setContent(commonContentDTO.getContent());
        discussPostService.updateDiscussPost(post);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> getPostsInBoard(Long boardId, BoardPostsPageDTO boardPostsPageDTO) {

        int limit = boardPostsPageDTO.getSafeLimit();
        int index = boardPostsPageDTO.getSafeIndex();
        int orderMode = boardPostsPageDTO.getSafeOrderMode();

        // orderMode 0: 按照时间排序 1: 按照热度排序
        // 总帖子数量，用于分页
        Long totalRows = discussPostService.getDiscussPostRows(0L, boardId);

        // 分页查询帖子
        // userId = 0 表示查询所有用户的帖子
        IPage<DiscussPost> pages = discussPostService.getDiscussPosts(
                0L, boardId, index, limit, orderMode);
        List<DiscussPost> postList = pages.getRecords();

        List<CurrentPageItemVO> discussPostListVOList = new ArrayList<>();
        CurrentPageVO currentPageVO = new CurrentPageVO();

        // 封装帖子+作者+点赞数
        if (!postList.isEmpty()) {
            for (DiscussPost post : postList) {
                CurrentPageItemVO currentPageItemVO = new CurrentPageItemVO();
                SimpleDiscussPostVO simpleDiscussPostVO = new SimpleDiscussPostVO();
                BeanUtils.copyProperties(post, simpleDiscussPostVO);
                currentPageItemVO.setDiscussPosts(simpleDiscussPostVO);

                User user = userService.getUserById(post.getUserId());
                SimpleUserVO userInfoVO = UserConverter.toSimpleVO(user);
                currentPageItemVO.setUser(userInfoVO);

                Long postCommentCount = commentService.getCommentCountByPostId(post.getId());
                System.out.println("查找 (" + post.getTitle() + ") postId为: " + post.getId() + " 的评论数量: " + postCommentCount);
                currentPageItemVO.setCommentCount(postCommentCount);

//                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
//                map.put("likeCount", likeCount);
                // 还没有封装点赞数量

                discussPostListVOList.add(currentPageItemVO);
            }
        }
        // 封装分页信息和数据
        currentPageVO.setTotalRows(totalRows);
        currentPageVO.setCurrentPage(pages.getCurrent());
        currentPageVO.setLimit(limit);
        currentPageVO.setDiscussPosts(discussPostListVOList);


        return ResultResponse.success(currentPageVO);
    }
}
