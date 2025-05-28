package com.yugao.converter;

import com.yugao.domain.permission.Permission;
import com.yugao.vo.auth.SimplePermissionVO;

public class PermissionConverter {

    public static SimplePermissionVO toSimplePermissionVO(Permission permission){
        SimplePermissionVO simplePermissionVO = new SimplePermissionVO();
        simplePermissionVO.setId(permission.getId());
        simplePermissionVO.setName(permission.getName());
        simplePermissionVO.setDescription(permission.getDescription());
        simplePermissionVO.setStatus(permission.getStatus());
        simplePermissionVO.setLevel(permission.getLevel());
        return simplePermissionVO;
    }
}
