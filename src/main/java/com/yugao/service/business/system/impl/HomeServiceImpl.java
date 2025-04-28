package com.yugao.service.business.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yugao.converter.UserConverter;
import com.yugao.domain.DiscussPost;
import com.yugao.domain.User;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.system.HomeService;
import com.yugao.service.data.CommentService;
import com.yugao.service.data.DiscussPostService;
import com.yugao.service.data.UserService;
import com.yugao.vo.post.CurrentPageItemVO;
import com.yugao.vo.post.CurrentPageVO;
import com.yugao.vo.user.SimpleUserVO;
import com.yugao.vo.user.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Override
    public ResponseEntity<ResultFormat> getIndexPage(int index, int limit, int orderMode) {
        // orderMode 0: 按照时间排序 1: 按照热度排序
        // 总帖子数量，用于分页
        Long totalRows = discussPostService.getDiscussPostRows(0L);

        // 分页查询帖子
        // userId = 0 表示查询所有用户的帖子
        IPage<DiscussPost> pages = discussPostService.getDiscussPosts(
                0L, index, limit, orderMode);
        List<DiscussPost> postList = pages.getRecords();

        List<CurrentPageItemVO> discussPostListVOList = new ArrayList<>();
        CurrentPageVO currentPageVO = new CurrentPageVO();

        // 封装帖子+作者+点赞数
        if (!postList.isEmpty()) {
            for (DiscussPost post : postList) {
                CurrentPageItemVO currentPageItemVO = new CurrentPageItemVO();
                currentPageItemVO.setDiscussPosts(post);

                User user = userService.getUserById(post.getUserId());
                SimpleUserVO userInfoVO = UserConverter.toSimpleVO(user);
                currentPageItemVO.setUser(userInfoVO);

                Long postCommentCount = commentService.getCommentCountByPostId(post.getId());
                System.out.println("查找 (" + post.getTitle() + ") postId为: " + post.getId() + " 的评论数量: " + postCommentCount);
                currentPageItemVO.setCommentCount(postCommentCount);

//                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
//                map.put("likeCount", likeCount);
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
