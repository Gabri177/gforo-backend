package com.yugao.service.business.post.impl;

import com.yugao.converter.DiscussPostConverter;
import com.yugao.domain.post.DiscussPost;
import com.yugao.dto.comment.CommonContentDTO;
import com.yugao.dto.post.NewDiscussPostDTO;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.builder.VOBuilder;
import com.yugao.service.business.post.PostService;
import com.yugao.service.data.BoardService;
import com.yugao.service.data.CommentService;
import com.yugao.service.data.DiscussPostService;
import com.yugao.service.handler.PostHandler;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.post.CurrentPageItemVO;
import com.yugao.vo.post.CurrentPageVO;
import com.yugao.vo.post.PostPageVO;
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
    private BoardService boardService;

    @Autowired
    private VOBuilder VOBuilder;

    @Override
    public ResponseEntity<ResultFormat> getPostDetail(Long postId, Long currentPage, Integer pageSize, Boolean isAsc) {

        System.out.println("getPostDetail: " + postId + " " + currentPage);
        PostPageVO postPageVO = new PostPageVO();
        postPageVO.setOriginalPost(postHandler.getOriginalPostDetail(postId));
        postPageVO.setReplies(postHandler.getCommentPostDetailList(postId, currentPage, pageSize, isAsc));
        postPageVO.setCurrentPage(currentPage);
        postPageVO.setTotalRows(commentService.getCommentCountByPostId(postId));
        postPageVO.setLimit(pageSize); // 暂时没用 以防万一

        return ResultResponse.success(postPageVO);
    }

    @Override
    public ResponseEntity<ResultFormat> publishPost(NewDiscussPostDTO newDiscussPostDTO) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        if (!boardService.isExistBoard(newDiscussPostDTO.getBoardId()))
            throw new BusinessException(ResultCodeEnum.BOARD_NOT_FOUND);
        DiscussPost newDiscussPost = DiscussPostConverter.newDiscussPostDTOtoDiscussPost(newDiscussPostDTO, userId);
        discussPostService.addDiscussPost(newDiscussPost);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> deletePost(Long postId) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        DiscussPost post = discussPostService.getDiscussPostById(postId);
        if (post == null)
            throw new BusinessException(ResultCodeEnum.POST_NOT_FOUND);
        if (!userId.equals(post.getUserId()))
            throw new BusinessException(ResultCodeEnum.USER_NOT_AUTHORIZED);
        discussPostService.deleteDiscussPost(postId);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> updatePost(CommonContentDTO commonContentDTO) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        DiscussPost post = discussPostService.getDiscussPostById(commonContentDTO.getId());
        if (post == null)
            throw new BusinessException(ResultCodeEnum.POST_NOT_FOUND);
        if (!userId.equals(post.getUserId()))
            throw new BusinessException(ResultCodeEnum.USER_NOT_AUTHORIZED);
        post.setTitle(commonContentDTO.getTitle());
        post.setContent(commonContentDTO.getContent());
        discussPostService.updateDiscussPost(post);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> getPosts(Long userId,
                                                 Long boardId,
                                                 Integer currentPage,
                                                 Integer pageSize,
                                                 Integer orderMode) {

        // orderMode 0: 按照时间排序 1: 按照热度排序
        // 总帖子数量，用于分页
        Long totalRows = discussPostService.getDiscussPostRows(userId, boardId);

        // 分页查询帖子
        // userId = 0 表示查询所有用户的帖子
        List<DiscussPost> postList = discussPostService.getDiscussPosts(
                userId, boardId, currentPage, pageSize, orderMode);

        List<CurrentPageItemVO> discussPostListVOList = new ArrayList<>();
        CurrentPageVO currentPageVO = new CurrentPageVO();

        // 封装帖子+作者+点赞数
        if (!postList.isEmpty()) {
            for (DiscussPost post : postList) {
                CurrentPageItemVO currentPageItemVO = VOBuilder.buildCurrentPageItemVO(post);

                discussPostListVOList.add(currentPageItemVO);
            }
        }
        // 封装分页信息和数据
        currentPageVO.setTotalRows(totalRows);
        currentPageVO.setCurrentPage(currentPage);
        currentPageVO.setLimit(pageSize);
        currentPageVO.setDiscussPosts(discussPostListVOList);


        return ResultResponse.success(currentPageVO);
    }
}
