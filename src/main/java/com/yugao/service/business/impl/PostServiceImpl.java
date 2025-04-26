package com.yugao.service.business.impl;

import com.yugao.domain.DiscussPost;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.PostService;
import com.yugao.service.data.CommentService;
import com.yugao.service.data.DiscussPostService;
import com.yugao.service.handler.PostHandler;
import com.yugao.vo.PostPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostHandler postHandler;

    @Autowired
    private CommentService commentService;

    @Override
    public ResponseEntity<ResultFormat> getPostDetail(Long postId, Long currentPage) {

        PostPageVO postPageVO = new PostPageVO();
        if (currentPage == 1)
            postPageVO.setOriginalPost(postHandler.getOriginalPostDetail(postId));
        else
            postPageVO.setOriginalPost(null);
        postPageVO.setReplies(postHandler.getCommentPostDetailList(postId, currentPage));
        postPageVO.setCurrentPage(currentPage);
        postPageVO.setTotalRows(commentService.getCommentCount(postId));
        postPageVO.setLimit(10); // 暂时没用 以防万一

        return ResultResponse.success(postPageVO);
    }
}
