package com.yugao.service.handler;

import com.yugao.converter.CommentConverter;
import com.yugao.converter.PostConverter;
import com.yugao.converter.TitleConverter;
import com.yugao.converter.UserConverter;
import com.yugao.domain.comment.Comment;
import com.yugao.domain.post.DiscussPost;
import com.yugao.domain.title.Title;
import com.yugao.domain.user.User;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.service.business.like.LikeService;
import com.yugao.service.data.comment.CommentService;
import com.yugao.service.data.post.DiscussPostService;
import com.yugao.service.data.title.TitleService;
import com.yugao.service.data.user.UserService;
import com.yugao.vo.comment.CommentVO;
import com.yugao.vo.post.PostDetailVO;
import com.yugao.vo.title.SimpleTitleVO;
import com.yugao.vo.user.SimpleUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostHandler {

    private final DiscussPostService discussPostService;
    private final CommentService commentService;
    private final UserService userService;
    private final LikeService likeService;
    private final UserHandler userHandler;
    private final TitleService titleService;

    private SimpleUserVO getAuthorInfo(Long userId) {

        User user = userHandler.getUser(userId);
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
            SimpleUserVO targetUserInfo = null;
            SimpleUserVO author = UserConverter.toSimpleVO(userMap.get(comment.getUserId()));

            if (comment.getTargetId() != 0L) {
                User targetUser = targetUserMap.get(comment.getTargetId());
                if (targetUser != null) {
                    targetUserInfo = UserConverter.toSimpleVO(targetUser);
                }
            }
            CommentVO vo = CommentConverter.toCommentVO(
                    comment,
                    targetUserInfo,
                    author,
                    likeService.countLikeComment(comment.getId()),
                    likeService.checkLikeComment(comment.getId())
            );
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
        DiscussPost originalPost = discussPostService.getDiscussPostById(postId);
        if (originalPost == null)
            throw new BusinessException(ResultCodeEnum.POST_NOT_FOUND);
        SimpleUserVO author = getAuthorInfo(originalPost.getUserId());
        List<CommentVO> replies = buildComments(postId);
        Integer likeCount = likeService.countLikePost(originalPost.getId());

        Title authorTitle = titleService.findTitleByUserId(originalPost.getUserId());
        SimpleTitleVO authorTitleVO = TitleConverter.toSimpleTitleVO(authorTitle);
        return PostConverter.toPostDetailVO(originalPost,
                author,
                replies,
                likeCount,
                likeService.checkLikePost(originalPost.getId()),
                authorTitleVO);
    }

    public List<PostDetailVO> getCommentPostDetailList(Long postId, Long currentPage, Integer pageSize, Boolean isAsc) {
        List<Comment> commentList = commentService.findCommentsToPost(postId, currentPage, pageSize, isAsc);
        if (commentList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> userIds = commentList.stream()
                .map(Comment::getUserId)
                .toList();
        Map<Long, User> userMap = getUserMap(userIds);

        List<PostDetailVO> postDetailVOList = new ArrayList<>();
        for (Comment comment : commentList) {

            Title authorTitle = titleService.findTitleByUserId(comment.getUserId());
            SimpleTitleVO authorTitleVO = TitleConverter.toSimpleTitleVO(authorTitle);

            PostDetailVO postDetailVO = PostConverter.toPostDetailVO(
                    comment,
                    UserConverter.toSimpleVO(userMap.get(comment.getUserId())),
                    buildCommentsForPostComments(comment.getId()),
                    likeService.countLikeComment(comment.getId()),
                    likeService.checkLikeComment(comment.getId()),
                    authorTitleVO
            );
            postDetailVOList.add(postDetailVO);
        }
        return postDetailVOList;
    }
}
