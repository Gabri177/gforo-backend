package com.yugao.service.builder;

import com.yugao.converter.CommentConverter;
import com.yugao.converter.DiscussPostConverter;
import com.yugao.converter.PermissionConverter;
import com.yugao.converter.UserConverter;
import com.yugao.domain.board.Board;
import com.yugao.domain.notification.NotificationRead;
import com.yugao.domain.post.DiscussPost;
import com.yugao.domain.permission.Role;
import com.yugao.domain.user.User;
import com.yugao.enums.EntityTypeEnum;
import com.yugao.service.business.like.LikeService;
import com.yugao.service.data.comment.CommentService;
import com.yugao.service.data.notification.NotificationReadService;
import com.yugao.service.data.permission.*;
import com.yugao.service.data.post.DiscussPostService;
import com.yugao.service.data.user.UserService;
import com.yugao.service.handler.PermissionHandler;
import com.yugao.service.handler.UserHandler;
import com.yugao.vo.auth.AccessControlVO;
import com.yugao.vo.auth.RoleDetailItemVO;
import com.yugao.vo.board.BoardInfosItemVO;
import com.yugao.vo.comment.SimpleCommentVO;
import com.yugao.vo.notification.UserNotificationVO;
import com.yugao.vo.post.CurrentPageItemVO;
import com.yugao.vo.post.SimpleDiscussPostVO;
import com.yugao.vo.user.SimpleUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class VOBuilder {

    private final DiscussPostService discussPostService;
    private final CommentService commentService;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final RolePermissionService rolePermissionService;
    private final UserRoleService userRoleService;
    private final BoardPosterService boardPosterService;
    private final PermissionHandler permissionHandler;
    private final LikeService likeService;
    private final UserHandler userHandler;
    private final UserService userService;
    private final NotificationReadService notificationReadService;


    public BoardInfosItemVO buildBoardInfosItemVO(Board board) {

        BoardInfosItemVO boardInfosItemVO = new BoardInfosItemVO();
        BeanUtils.copyProperties(board, boardInfosItemVO);
        boardInfosItemVO.setPostCount(discussPostService.getDiscussPostRows(0L, board.getId()));
        List<Long> postIds = discussPostService.getDiscussPostIdsByBoardId(board.getId());
        Long commentCount = commentService.getCommentCountByPostIds(postIds);
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

        //111111111111111111111
        User user = userHandler.getUser(post.getUserId());
        SimpleUserVO userInfoVO = UserConverter.toSimpleVO(user);
        currentPageItemVO.setUser(userInfoVO);
        //System.out.println("用户简单信息: " + userInfoVO);

        Long postCommentCount = commentService.getCommentCountByPostId(post.getId());
        //System.out.println("查找 (" + post.getTitle() + ") postId为: " + post.getId() + " 的评论数量: " + postCommentCount);
        currentPageItemVO.setCommentCount(postCommentCount);

        currentPageItemVO.setLikeCount(likeService.countLikePost(post.getId()));
        // 还没有封装点赞数量

        return currentPageItemVO;
    }

    public AccessControlVO buildAccessControlVO(Long userId){

        AccessControlVO accessControlVO = new AccessControlVO();
        accessControlVO.setPermissions(permissionHandler.getPermissionCodesByUserId(userId));
        List<Long> roleIds = userRoleService.getRoleIdsByUserId(userId);
        accessControlVO.setRoles(roleService.getRoleNamesByIds(roleIds));
        accessControlVO.setBoardIds(boardPosterService.getBoardIdsByUserId(userId));
        return accessControlVO;
    }

    public RoleDetailItemVO buildRoleDetailItemVO(Role role){
        RoleDetailItemVO roleDetailItemVO = new RoleDetailItemVO();
        roleDetailItemVO.setId(role.getId());
        roleDetailItemVO.setName(role.getName());
        roleDetailItemVO.setDescription(role.getDescription());
        roleDetailItemVO.setStatus(role.getStatus());
        roleDetailItemVO.setCreateTime(role.getCreateTime());
        roleDetailItemVO.setLevel(role.getLevel());
        roleDetailItemVO.setBuildin(role.getBuildin());
        roleDetailItemVO.setPermissions(
                permissionService.getPermissionsByIds(
                                rolePermissionService.getPermissionIdsByRoleId(role.getId()))
                        .stream()
                        .map(PermissionConverter::toSimplePermissionVO)
                        .collect(Collectors.toList())
        );
        return roleDetailItemVO;
    }

    public List<UserNotificationVO> assembleUserNotificationListVO(List<UserNotificationVO> res, Long userId) {

        // 获取是否已读
        List<Long> readIds = notificationReadService.getByUserId(userId)
                .stream()
                .map(NotificationRead::getNotificationId)
                .toList();
//        System.out.println("已读的通知列表为" + readIds);
        // 获取作者信息
        List<Long> authorIds = res.stream()
                .map(UserNotificationVO::getSenderId)
                .distinct()
                .toList();
        Map<Long, SimpleUserVO> authorMap = userService.getUsersByIds(authorIds)
                .stream()
                .map(UserConverter::toSimpleVO)
                .collect(Collectors.toMap(SimpleUserVO::getId, Function.identity()));
        // 获取实体信息
        // 获取实体类型是comment的commentId的list
//        System.out.println("组装实体类型是comment的commentId的list");
        Map<Long, SimpleCommentVO> commentMap = commentService.findCommentsByIds(
                        res.stream()
                                .filter(notification -> notification.getEntityType() == EntityTypeEnum.COMMENT)
                                .map(UserNotificationVO::getEntityId)
                                .distinct()
                                .toList()
                )
                .stream()
                .map(CommentConverter::toSimpleCommentVO)
                .collect(Collectors.toMap(SimpleCommentVO::getId, Function.identity()));

        // 获取实体类型是post的postId的list

        // 1）通知中直接是帖子的 entityId
        List<Long> postIdsFromNotification = res.stream()
                .filter(notification -> notification.getEntityType() == EntityTypeEnum.POST)
                .map(UserNotificationVO::getEntityId)
                .toList();
        // 2）评论中关联的 postId
        List<Long> postIdsFromComments = commentMap.values().stream()
                .map(SimpleCommentVO::getPostId)
                .toList();
        // 合并并去重
        Set<Long> allPostIds = Stream.concat(postIdsFromNotification.stream(), postIdsFromComments.stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
//        System.out.println("组装实体类型是post的postId的list");
        Map<Long, SimpleDiscussPostVO> postMap = discussPostService.getDiscussPostsByIds(new ArrayList<>(allPostIds))
                .stream()
                .map(DiscussPostConverter::toSimpleDiscussPostVO)
                .collect(Collectors.toMap(SimpleDiscussPostVO::getId, Function.identity()));
        // 装配已读以及作者信息
//        System.out.println("装配已读以及作者信息");
        res.forEach(notification -> {
            notification.setIsRead(readIds.contains(notification.getId()));
            notification.setAuthor(authorMap.get(notification.getSenderId()));
            if (notification.getEntityType() == EntityTypeEnum.COMMENT) {
                notification.setComment(commentMap.get(notification.getEntityId()));
                notification.setPost(postMap.get(
                        Optional.ofNullable(commentMap.get(notification.getEntityId()))
                                .map(SimpleCommentVO::getPostId)
                                .orElse(null)
                        )
                );
            } else if (notification.getEntityType() == EntityTypeEnum.POST) {
                notification.setPost(postMap.get(notification.getEntityId()));
            }
        });
        return res;
    }

}
