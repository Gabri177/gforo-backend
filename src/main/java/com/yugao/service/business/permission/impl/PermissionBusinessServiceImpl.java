package com.yugao.service.business.permission.impl;

import com.yugao.domain.BoardPoster;
import com.yugao.domain.UserRole;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.enums.RoleEnum;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.builder.VOBuilder;
import com.yugao.service.business.permission.PermissionBusinessService;
import com.yugao.service.data.BoardPosterService;
import com.yugao.service.data.PermissionService;
import com.yugao.service.data.RolePermissionService;
import com.yugao.service.data.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionBusinessServiceImpl implements PermissionBusinessService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private BoardPosterService boardPosterService;

    @Override
    public List<String> getPermissionCodesByUserId(Long userId) {

        // 1. 获取用户的所有角色
        List<Long> roleIds = userRoleService.getRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) return List.of();

        // 2. 获取角色对应的所有权限id
        List<Long> permissionIds = rolePermissionService.getPermissionIdsByRoleIds(roleIds);
        if (permissionIds.isEmpty()) return List.of();

        // 3. 获取所有有效权限的code
        return permissionService.getCodesByIds(permissionIds);
    }

    @Transactional
    @Override
    public Boolean setBoardPoster(Long userId, Long boardId) {

        List<Long> userRoleIds = userRoleService.getRoleIdsByUserId(userId);
        List<Long> boardPosterIds = boardPosterService.getBoardIdsByUserId(userId);
        if (!userRoleIds.contains(RoleEnum.ROLE_BOARD.getCode()) &&
                !userRoleService.addUserRole(new UserRole(userId, RoleEnum.ROLE_BOARD)))
            throw new BusinessException(ResultCodeEnum.SQL_UPDATING_ERROR);
        if (!boardPosterIds.contains(boardId) &&
                !boardPosterService.addBoardPoster(new BoardPoster(userId, boardId)))
            throw new BusinessException(ResultCodeEnum.SQL_UPDATING_ERROR);
        return true;
    }

    @Transactional
    @Override
    public Boolean deleteBoardPoster(Long userId, Long boardId) {

        List<Long> userRoleIds = userRoleService.getRoleIdsByUserId(userId);
        List<Long> boardPosterIds = boardPosterService.getBoardIdsByUserId(userId);
        if (userRoleIds.contains(RoleEnum.ROLE_BOARD.getCode()) &&
                userRoleService.deleteUserRole(new UserRole(userId, RoleEnum.ROLE_BOARD)))
            throw new BusinessException(ResultCodeEnum.SQL_UPDATING_ERROR);
        if (boardPosterIds.contains(boardId) &&
                boardPosterService.deleteBoardPoster(new BoardPoster(userId, boardId)))
            throw new BusinessException(ResultCodeEnum.SQL_UPDATING_ERROR);
        return true;
    }

    @Transactional
    @Override
    public Boolean updatePermissionsToRole(Long roleId, List<Long> permissionIds) {

        // 1. 获取角色的所有权限
        List<Long> rolePermissionIds = rolePermissionService.getPermissionIdsByRoleId(roleId);
        // 2. 删除角色的所有权限
        if (!rolePermissionIds.isEmpty() && !rolePermissionService.deleteRolePermissions(roleId, rolePermissionIds))
            throw new BusinessException(ResultCodeEnum.SQL_UPDATING_ERROR);
        // 3. 添加角色的所有权限
        if (!permissionIds.isEmpty() && !rolePermissionService.addRolePermissions(roleId, permissionIds))
            throw new BusinessException(ResultCodeEnum.SQL_UPDATING_ERROR);
        return true;
    }

    @Override
    public ResponseEntity<ResultFormat> getRoleDetailList(Long roleId){
        return null;
    }

}
