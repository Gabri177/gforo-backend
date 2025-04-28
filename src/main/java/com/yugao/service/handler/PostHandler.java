package com.yugao.service.handler;

import com.yugao.converter.PostConverter;
import com.yugao.converter.UserConverter;
import com.yugao.domain.Comment;
import com.yugao.domain.DiscussPost;
import com.yugao.domain.User;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.service.data.CommentService;
import com.yugao.service.data.DiscussPostService;
import com.yugao.service.data.UserService;
import com.yugao.vo.comment.CommentVO;
import com.yugao.vo.post.PostDetailVO;
import com.yugao.vo.user.SimpleUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PostHandler {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    private SimpleUserVO existUserById(Long userId) {
        // 这里可以尝试返回一个默认位置用户的对象
        User user = userService.getUserById(userId);
        if (user == null)
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        return UserConverter.toSimpleVO(user);
    }

    private void patchMissingUsers(List<Long> ids, Map<Long, User> userMap) {
        List<Long> missingUserIds = ids.stream()
                .filter(id -> !userMap.containsKey(id))
                .distinct()
                .toList();
        if (!missingUserIds.isEmpty()) {
            List<User> missingUsers = userService.getUsersByIds(missingUserIds);
            for (User missing : missingUsers) {
                userMap.put(missing.getId(), missing);
            }
        }
    }

    private Map<Long, User> getUserMap(List<Long> ids) {
        List<User> users = userService.getUsersByIds(ids);
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        patchMissingUsers(ids, userMap);
        return userMap;
    }

    private List<CommentVO> convertCommentsToVOs(List<Comment> commentList) {
        if (commentList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> userIds = commentList.stream()
                .map(Comment::getUserId)
                .toList();
        List<Long> targetIds = commentList.stream()
                .map(Comment::getTargetId)
                .filter(id -> id != 0L)
                .toList();

        Map<Long, User> userMap = getUserMap(userIds);
        Map<Long, User> targetUserMap = getUserMap(targetIds);

        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentVO vo = new CommentVO();
            vo.setId(comment.getId());
            vo.setContent(comment.getContent());
            vo.setCreateTime(comment.getCreateTime());
            vo.setIsExpanded(false);
            vo.setAuthor(UserConverter.toSimpleVO(userMap.get(comment.getUserId())));

            if (comment.getTargetId() != 0L) {
                User targetUser = targetUserMap.get(comment.getTargetId());
                if (targetUser != null) {
                    vo.setTargetUserInfo(UserConverter.toSimpleVO(targetUser));
                }
            }
            commentVOList.add(vo);
        }
        return commentVOList;
    }

    private List<CommentVO> buildComments(Long postId) {
        List<Comment> commentList = commentService.findCommentsToPostFloor(postId);
        return convertCommentsToVOs(commentList);
    }

    private List<CommentVO> buildCommentsForPostComments(Long commentId) {
        List<Comment> commentList = commentService.findCommentListOfComment(commentId);
        return convertCommentsToVOs(commentList);
    }

    public PostDetailVO getOriginalPostDetail(Long postId) {
        System.out.println("getOriginalPostDetail: " + postId);
        DiscussPost originalPost = discussPostService.getDiscussPostById(postId);
        if (originalPost == null)
            throw new BusinessException(ResultCode.POST_NOT_FOUND);

        SimpleUserVO author = existUserById(originalPost.getUserId());
        List<CommentVO> replies = buildComments(postId);

        return PostConverter.toPostDetailVO(originalPost, author, replies, false);
    }

    public List<PostDetailVO> getCommentPostDetailList(Long postId, Long currentPage) {
        List<Comment> commentList = commentService.findCommentsToPost(postId, currentPage, 10);
        if (commentList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> userIds = commentList.stream()
                .map(Comment::getUserId)
                .toList();
        Map<Long, User> userMap = getUserMap(userIds);

        List<PostDetailVO> postDetailVOList = new ArrayList<>();
        for (Comment comment : commentList) {
            PostDetailVO postDetailVO = new PostDetailVO();
            postDetailVO.setId(comment.getId());
            postDetailVO.setContent(comment.getContent());
            postDetailVO.setCreateTime(comment.getCreateTime());
            postDetailVO.setIsExpanded(false);

            User user = userMap.get(comment.getUserId());
            postDetailVO.setAuthor(UserConverter.toSimpleVO(user));

            List<CommentVO> replies = buildCommentsForPostComments(comment.getId());
            postDetailVO.setReplies(replies);

            postDetailVOList.add(postDetailVO);
        }
        return postDetailVOList;
    }
}
