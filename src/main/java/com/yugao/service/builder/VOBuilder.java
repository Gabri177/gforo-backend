package com.yugao.service.builder;

import com.yugao.converter.PermissionConverter;
import com.yugao.converter.UserConverter;
import com.yugao.domain.board.Board;
import com.yugao.domain.post.DiscussPost;
import com.yugao.domain.permission.Role;
import com.yugao.domain.user.User;
import com.yugao.service.business.post.LikeService;
import com.yugao.service.data.*;
import com.yugao.service.handler.PermissionHandler;
import com.yugao.vo.auth.AccessControlVO;
import com.yugao.vo.auth.RoleDetailItemVO;
import com.yugao.vo.board.BoardInfosItemVO;
import com.yugao.vo.post.CurrentPageItemVO;
import com.yugao.vo.post.SimpleDiscussPostVO;
import com.yugao.vo.user.SimpleUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VOBuilder {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private BoardPosterService boardPosterService;

    @Autowired
    private PermissionHandler permissionHandler;

    @Autowired
    private LikeService likeService;


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

        User user = userService.getUserById(post.getUserId());
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
        roleDetailItemVO.setPermissions(
                permissionService.getPermissionsByIds(
                                rolePermissionService.getPermissionIdsByRoleId(role.getId()))
                        .stream()
                        .map(PermissionConverter::toSimplePermissionVO)
                        .collect(Collectors.toList())
        );
        return roleDetailItemVO;
    }

}
