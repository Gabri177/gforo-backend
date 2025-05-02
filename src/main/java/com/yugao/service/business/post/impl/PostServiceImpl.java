package com.yugao.service.business.post.impl;

import com.yugao.converter.DiscussPostConverter;
import com.yugao.domain.DiscussPost;
import com.yugao.dto.comment.CommonContentDTO;
import com.yugao.dto.post.NewDiscussPostDTO;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.post.PostService;
import com.yugao.service.data.CommentService;
import com.yugao.service.data.DiscussPostService;
import com.yugao.service.handler.PostHandler;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.post.PostPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostHandler postHandler;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

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
}
