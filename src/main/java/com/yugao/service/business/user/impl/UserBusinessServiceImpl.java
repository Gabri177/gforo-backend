package com.yugao.service.business.user.impl;

import com.yugao.constants.KafkaEventType;
import com.yugao.constants.RedisKeyConstants;
import com.yugao.converter.CommentConverter;
import com.yugao.converter.PostConverter;
import com.yugao.converter.UserConverter;
import com.yugao.domain.comment.Comment;
import com.yugao.domain.post.DiscussPost;
import com.yugao.domain.user.User;
import com.yugao.dto.auth.ActiveAccountDTO;
import com.yugao.dto.user.UserChangePasswordDTO;
import com.yugao.dto.user.UserChangeUsernameDTO;
import com.yugao.dto.user.UserInfoUpdateDTO;
import com.yugao.dto.auth.UserVerifyEmailDTO;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.builder.EmailBuilder;
import com.yugao.service.builder.VOBuilder;
import com.yugao.service.business.captcha.CaptchaService;
import com.yugao.service.business.like.LikeService;
import com.yugao.service.business.user.UserBusinessService;
import com.yugao.service.data.*;
import com.yugao.service.handler.EventHandler;
import com.yugao.service.handler.TokenHandler;
import com.yugao.service.handler.UserHandler;
import com.yugao.service.limiter.EmailRateLimiter;
import com.yugao.service.validator.CaptchaValidator;
import com.yugao.service.validator.UserValidator;
import com.yugao.util.security.PasswordUtil;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.post.CurrentPageItemVO;
import com.yugao.vo.post.CurrentPageVO;
import com.yugao.vo.post.SimpleDiscussPostVO;
import com.yugao.vo.user.SimpleUserVO;
import com.yugao.vo.user.UserCommentsItemVO;
import com.yugao.vo.user.UserCommentsVO;
import com.yugao.vo.user.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBusinessServiceImpl implements UserBusinessService {

    private final UserService userService;
    private final UserValidator userValidator;
    private final EmailRateLimiter emailRateLimiter;
    private final EmailBuilder emailBuilder;
    private final DiscussPostService discussPostService;
    private final CommentService commentService;
    private final CaptchaValidator captchaValidator;
    private final CaptchaService captchaService;
    private final VOBuilder voBuilder;
    private final LikeService likeService;
    private final UserHandler userHandler;
    private final TokenHandler tokenHandler;
    private final EventHandler eventHandler;


    @Override
    public ResponseEntity<ResultFormat> getUserInfo(Long userId) {


        Long curUserId = SecurityUtils.mustGetLoginUserId();
        if (userId != null && !userId.equals(curUserId))
            curUserId = userId;


        User userDomain = userValidator.validateUserIdExists(curUserId);

        UserInfoVO userInfoVO;
        if (SecurityUtils.mustGetLoginUserId().equals(userId) || userId == null)
            userInfoVO = UserConverter.toUserInfoVO(userDomain, false);
        else
            userInfoVO = UserConverter.toUserInfoVO(userDomain, true);
        userInfoVO.setPostCount(discussPostService.getDiscussPostRows(curUserId, 0L));
        userInfoVO.setCommentCount(commentService.getCommentCountByUserId(curUserId));
        userInfoVO.setAccessControl(voBuilder.buildAccessControlVO(curUserId));
        userInfoVO.setGetLikeCount(likeService.countUserGetLikes(curUserId));
        return ResultResponse.success(userInfoVO);
    }

    @Override
    public ResponseEntity<ResultFormat> changePassword(UserChangePasswordDTO userChangePasswordDTO) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        User userDomain = userValidator.validateUserIdExists(userId);
        userValidator.validatePasswordChange(userDomain, userChangePasswordDTO);
        String newRawPassword = userChangePasswordDTO.getNewPassword();
        if (!userService.updatePassword(userId, PasswordUtil.encode(newRawPassword)))
            throw new BusinessException(ResultCodeEnum.USER_PASSWORD_UPDATE_ERROR);
        // 更新缓存
        userHandler.updateUserCache(userId);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> updateUserInfo(UserInfoUpdateDTO userInfoUpdateDTO) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        User userDomain = userValidator.validateUserIdExists(userId);
        if(!userService.updateUserProfile(userDomain.getId(), userInfoUpdateDTO))
            throw new BusinessException(ResultCodeEnum.USER_PROFILE_UPDATE_ERROR);
        // 更新缓存
        userHandler.updateUserCache(userId);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> sendVerifyEmail(UserVerifyEmailDTO userVerifyEmailDTO) {

        Long currentUserId = SecurityUtils.mustGetLoginUserId();
        userValidator.validateEmailChangeInterval(currentUserId);
        emailRateLimiter.check(userVerifyEmailDTO.getEmail());
        userValidator.validateEmailExists(userVerifyEmailDTO.getEmail());
        String code = captchaService.generateSixDigitCaptcha(
                RedisKeyConstants.CHANGE_EMAIL,
                userVerifyEmailDTO.getEmail());
        System.out.println("code = " + code);
        String html = emailBuilder.buildSixCodeVerifyHtml(code);
        // 将事件发送到Kafka 异步消费
        eventHandler.sendHtmlEmail(
                userVerifyEmailDTO.getEmail(),
                "GForo: Change Email",
                html,
                KafkaEventType.CHANGE_EMAIL
        );

        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> verifyEmail(ActiveAccountDTO activeAccountDTO) {

        System.out.println("activeAccountDTO = " + activeAccountDTO);
        Long currentUserId = SecurityUtils.mustGetLoginUserId();
        captchaValidator.validateSixDigitCaptcha(
                RedisKeyConstants.CHANGE_EMAIL,
                activeAccountDTO.getEmail(),
                activeAccountDTO.getSixDigitCaptcha());
        userValidator.validateUserIdExists(currentUserId);
        if (!userService.updateEmail(currentUserId, activeAccountDTO.getEmail()))
            throw new BusinessException(ResultCodeEnum.USER_EMAIL_UPDATE_ERROR);
        // 更新缓存
        userHandler.updateUserCache(currentUserId);

        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> changeUsername(UserChangeUsernameDTO userChangeUsernameDTO) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        userValidator.validateUsernameChangeInterval(userId);
        if (!userService.updateUsername(userId, userChangeUsernameDTO.getUsername()))
            throw new BusinessException(ResultCodeEnum.USER_USERNAME_UPDATE_ERROR);
        // 更新缓存
        userHandler.updateUserCache(userId);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> getCommentsByUserId(Integer currentPage, Integer pageSize, Boolean isAsc) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        UserCommentsVO userCommentsVO = new UserCommentsVO();
        List<UserCommentsItemVO> userCommentsItemVOs = new ArrayList<>();
        // 所需要的全部评论
        List<Comment> comments = commentService.getCommentListByUserId(
                userId,
                currentPage,
                pageSize,
                isAsc
        );
        // 所需要的所有author id
        Set<Long> authorIds = comments.stream()
                .map(Comment::getUserId)
                .filter(id -> id != 0L)
                .collect(Collectors.toSet());
        Map<Long, User> authorMap = userService.getUsersByIds(authorIds.stream().toList())
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        // 所需要的所有target id
        Set<Long> targetIds = comments.stream()
                .map(Comment::getTargetId)
                .filter(id -> id != 0L)
                .collect(Collectors.toSet());
        Map<Long, User> targetUserMap = userService.getUsersByIds(targetIds.stream().toList())
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        // 所需要的全部帖子
        Set<Long> postIds = comments.stream()
                .map(Comment::getPostId)
                .collect(Collectors.toSet());
        List<SimpleDiscussPostVO> posts = discussPostService.getDiscussPostsByIds(postIds.stream().toList())
                .stream()
                .map(PostConverter::toSimpleDiscussPostVO)
                .toList();
        Map<Long, SimpleDiscussPostVO> postMap = posts.stream()
                .collect(Collectors.toMap(SimpleDiscussPostVO::getId, Function.identity()));
        // 组装
        comments.forEach(comment -> {
            UserCommentsItemVO userCommentsItemVO = new UserCommentsItemVO();
            SimpleDiscussPostVO postInfo = postMap.get(comment.getPostId());
            if (postInfo == null)
                postInfo = PostConverter.toSimpleDiscussPostVO(DiscussPost.createGhostDiscusspost());
            userCommentsItemVO.setPostInfo(postInfo);
            SimpleUserVO targetUserInfo = null;
            if (targetUserMap.get(comment.getTargetId()) != null)
                targetUserInfo = UserConverter.toSimpleVO(targetUserMap.get(comment.getTargetId()));
            userCommentsItemVO.setCommentInfo(
                    CommentConverter.toCommentVO(
                            comment,
                            targetUserInfo,
                            UserConverter.toSimpleVO(authorMap.get(comment.getUserId())),
                            likeService.countLikeComment(comment.getId()),
                            likeService.checkLikeComment(comment.getId())
                    )
            );
            userCommentsItemVOs.add(userCommentsItemVO);
        });
        userCommentsVO.setCommentsList(userCommentsItemVOs);
        userCommentsVO.setCurrentPage(currentPage);
        userCommentsVO.setPageSize(pageSize);
        userCommentsVO.setTotalRows(commentService.getCommentCountByUserId(userId));
        return ResultResponse.success(userCommentsVO);
    }

    @Override
    public ResponseEntity<ResultFormat> getPostsByUserId(Long userId, Integer currentPage, Integer pageSize, Boolean isAsc) {

        User tarUser = userHandler.getUser(userId);
        if (tarUser == null)
            userId = SecurityUtils.mustGetLoginUserId();

        // 总帖子数量，用于分页
        Long totalRows = discussPostService.getDiscussPostRows(userId, 0L);

        // 分页查询帖子
        // userId = 0 表示查询所有用户的帖子
        List<DiscussPost> postList = discussPostService.getDiscussPosts(
                userId, 0L, currentPage, pageSize, 0);

        List<CurrentPageItemVO> discussPostListVOList = new ArrayList<>();
        CurrentPageVO currentPageVO = new CurrentPageVO();

        // 封装帖子+作者+点赞数
        if (!postList.isEmpty()) {
            for (DiscussPost post : postList) {
                CurrentPageItemVO currentPageItemVO = voBuilder.buildCurrentPageItemVO(post);

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
    public ResponseEntity<ResultFormat> logout(String deviceId) {

        Long userId = SecurityUtils.mustGetLoginUserId();
//        Boolean isLogout = userTokenService.deleteUserTokenByUserId(userId);
        tokenHandler.forceLogout(userId, deviceId);
        return ResultResponse.success(null);
    }
}
