package com.yugao.service.business.post.impl;

import com.yugao.converter.DiscussPostConverter;
import com.yugao.domain.board.Board;
import com.yugao.domain.post.DiscussPost;
import com.yugao.dto.comment.CommonContentDTO;
import com.yugao.dto.post.NewDiscussPostDTO;
import com.yugao.enums.EntityTypeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.security.LoginUser;
import com.yugao.service.builder.VOBuilder;
import com.yugao.service.business.post.PostService;
import com.yugao.service.business.search.ElasticSearchService;
import com.yugao.service.data.permission.BoardPosterService;
import com.yugao.service.data.board.BoardService;
import com.yugao.service.data.comment.CommentService;
import com.yugao.service.data.post.DiscussPostService;
import com.yugao.service.handler.EventHandler;
import com.yugao.service.handler.PostHandler;
import com.yugao.service.handler.VisitStatisticsHandler;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.post.CurrentPageItemVO;
import com.yugao.vo.post.CurrentPageVO;
import com.yugao.vo.post.PostPageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostHandler postHandler;
    private final CommentService commentService;
    private final DiscussPostService discussPostService;
    private final BoardService boardService;
    private final VOBuilder VOBuilder;
    private final VisitStatisticsHandler visitStatisticsHandler;
    private final BoardPosterService boardPosterService;
    private final EventHandler eventHandler;
    private final ElasticSearchService elasticSearchService;

    @Override
    public ResponseEntity<ResultFormat> getPostDetail(Long postId, Long currentPage, Integer pageSize, Boolean isAsc) {

        visitStatisticsHandler.recordVisit(SecurityUtils.getLoginUserId());
//        System.out.println("getPostDetail: " + postId + " " + currentPage);
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
        DiscussPost newDiscussPost = DiscussPostConverter.toDiscussPost(newDiscussPostDTO, userId);
        discussPostService.addDiscussPost(newDiscussPost);

        // 发送经验变动事件
        eventHandler.handleExpChange(userId, EntityTypeEnum.POST, newDiscussPost.getId(), 3);
        // 异步存储到搜索引擎
        eventHandler.handleSavePost(newDiscussPost);
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

        // 发送经验变动事件
        eventHandler.handleExpChange(userId,  EntityTypeEnum.POST, postId, -3);
        // 异步删除搜索引擎中的帖子
        eventHandler.handleDeletePost(postId);
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

        // 异步更新搜索引擎中的帖子
        eventHandler.handleSavePost(post);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> getPosts(Long userId,
                                                 Long boardId,
                                                 Integer currentPage,
                                                 Integer pageSize,
                                                 Integer orderMode) {

        visitStatisticsHandler.recordVisit(SecurityUtils.getLoginUserId());
        // orderMode 0: 按照时间排序 1: 按照热度排序
        // 总帖子数量，用于分页
        Board board = boardService.getBoardById(boardId);
        if (board == null || board.getStatus() != StatusEnum.NORMAL)
            throw new BusinessException(ResultCodeEnum.BOARD_NOT_FOUND);
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

    @Override
    public ResponseEntity<ResultFormat> changePostType(Long postId, Integer type) {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        DiscussPost post = discussPostService.getDiscussPostById(postId);
        if (post == null)
            throw new BusinessException(ResultCodeEnum.POST_NOT_FOUND);
        if (loginUser == null)
            throw new BusinessException(ResultCodeEnum.USER_NOT_LOGIN);
        if (loginUser.hasAuthority("post:change-type:board") &&
                !loginUser.hasAuthority("post:change-type:any")) {
            List<Long> postIds = boardPosterService.getBoardIdsByUserId(loginUser.getId());
            System.out.println("postIds: " + postIds);
            System.out.println("post.getBoardId(): " + post.getBoardId());
            if (!postIds.contains(post.getBoardId()))
                throw new BusinessException(ResultCodeEnum.NO_PERMISSION);
        }
        if (type == 0 || type == 1 || type == 2 || type == 3)
            discussPostService.updateType(postId, type);
        else
            throw new BusinessException(ResultCodeEnum.PARAMETER_ERROR);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> searchPost(String keyword, Integer currentPage, Integer pageSize) {

        // 总帖子数量，用于分页
        Long totalRows = elasticSearchService.countPostByKeyword(keyword);

        // 分页查询帖子
        // userId = 0 表示查询所有用户的帖子
        List<DiscussPost> postList = elasticSearchService.searchPost(keyword, currentPage, pageSize);

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
