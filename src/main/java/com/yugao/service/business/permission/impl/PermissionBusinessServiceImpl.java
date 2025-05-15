package com.yugao.service.business.permission.impl;

import com.yugao.converter.PermissionConverter;
import com.yugao.converter.RoleConverter;
import com.yugao.domain.permission.BoardPoster;
import com.yugao.domain.permission.Role;
import com.yugao.domain.permission.UserRole;
import com.yugao.dto.permission.AddNewRoleDTO;
import com.yugao.dto.permission.UpdateRolePermissionDTO;
import com.yugao.dto.permission.UpdateUserRoleDTO;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.builder.VOBuilder;
import com.yugao.service.business.permission.PermissionBusinessService;
import com.yugao.service.data.*;
import com.yugao.service.handler.PermissionHandler;
import com.yugao.service.validator.BoardValidator;
import com.yugao.service.validator.PermissionValidator;
import com.yugao.service.validator.RoleValidator;
import com.yugao.service.validator.UserValidator;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.auth.RoleDetailItemVO;
import com.yugao.vo.auth.RoleDetailsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionBusinessServiceImpl implements PermissionBusinessService {

    private final RoleService roleService;
    private final VOBuilder voBuilder;
    private final UserValidator userValidator;
    private final RoleValidator roleValidator;
    private final BoardValidator boardValidator;
    private final UserRoleService userRoleService;
    private final BoardPosterService boardPosterService;
    private final PermissionHandler permissionHandler;
    private final PermissionService permissionService;
    private final PermissionValidator permissionValidator;

    @Override
    public ResponseEntity<ResultFormat> getRoleDetailList(){

        Integer roleLevel = SecurityUtils.getUserLevel();
        RoleDetailsVO roleDetailsVO = new RoleDetailsVO();
        List<Role> roles = roleService.getAllRoles()
                .stream()
                .filter(role -> role.getLevel() > roleLevel)
                .toList();
        List<RoleDetailItemVO> roleDetailItemVOS = roles.stream()
                .map(voBuilder::buildRoleDetailItemVO)
                .toList();
        roleDetailsVO.setRoleDetailsList(roleDetailItemVOS);
        roleDetailsVO.setPermissionsList(
                permissionService.getAllPermissions()
                        .stream()
                        .map(PermissionConverter::toSimplePermissionVO)
                        .filter(pms -> pms.getLevel() > roleLevel)
                        .toList()
        );
        return ResultResponse.success(roleDetailsVO);
    }

    @Transactional
    @Override
    public ResponseEntity<ResultFormat> updateUserRole(UpdateUserRoleDTO updateUserRoleDTO) {

        System.out.println("updateRoleDTO: " + updateUserRoleDTO);
        // 1. 校验被更新的用户是否存在
        userValidator.validateUserIdExists(updateUserRoleDTO.getUserId());
        roleValidator.checkLevel(permissionHandler.getUserRoleLevel(updateUserRoleDTO.getUserId()));
        // 2. 校验被更新的角色是否存在
        roleValidator.checkRoleIds(updateUserRoleDTO.getRoleIds());
        // 3. 如果boardId存在 则校验boardId是否存在
        if (updateUserRoleDTO.getBoardIds() != null && !updateUserRoleDTO.getBoardIds().isEmpty())
            boardValidator.checkBoardIds(updateUserRoleDTO.getBoardIds());
        // 4. 更新用户角色 user_role board_poster
        userRoleService.deleteUserRolesByUserId(updateUserRoleDTO.getUserId());
        userRoleService.addUserRoles(updateUserRoleDTO.getRoleIds()
                .stream()
                .map(item -> new UserRole(updateUserRoleDTO.getUserId(), item))
                .toList()
        );
        if (updateUserRoleDTO.getBoardIds() != null) {
            boardPosterService.deleteBoardPosterByUserId(updateUserRoleDTO.getUserId());
            boardPosterService.addBoardPosters(updateUserRoleDTO.getBoardIds()
                    .stream()
                    .map(item -> new BoardPoster(updateUserRoleDTO.getUserId(), item))
                    .toList()
            );
        }
        return ResultResponse.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity<ResultFormat> updateRolePermission(UpdateRolePermissionDTO updateRolePermissionDTO) {

        if (SecurityUtils.getUserLevel() > updateRolePermissionDTO.getRoleLevel())
            throw new BusinessException(ResultCodeEnum.ROLE_LEVEL_NOT_ENOUGH);
        roleValidator.checkRoleId(updateRolePermissionDTO.getRoleId());
        roleValidator.checkLevel(updateRolePermissionDTO.getRoleLevel());
        System.out.println("level checked success");
        permissionValidator.checkPermissionIds(updateRolePermissionDTO.getPermissionIds());
        roleService.updateRole(RoleConverter.toRole(updateRolePermissionDTO));
        permissionHandler.updateRolePermission(
                updateRolePermissionDTO.getRoleId(),
                updateRolePermissionDTO.getPermissionIds());
        return ResultResponse.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity<ResultFormat> addNewRole(AddNewRoleDTO addNewRoleDTO) {

        roleValidator.checkLevel(addNewRoleDTO.getRoleLevel());
        roleValidator.checkRoleName(addNewRoleDTO.getRoleName());
        permissionValidator.checkPermissionIds(addNewRoleDTO.getPermissionIds());
        Role role = RoleConverter.toRole(addNewRoleDTO);
        roleService.addRole(role);
        permissionHandler.updateRolePermission(role.getId(), addNewRoleDTO.getPermissionIds());
        return ResultResponse.success(null);
    }

}
