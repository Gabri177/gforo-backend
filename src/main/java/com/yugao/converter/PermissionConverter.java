package com.yugao.converter;

import com.yugao.domain.Permission;
import com.yugao.domain.Role;
import com.yugao.vo.auth.SimplePermissionVO;

public class PermissionConverter {

    public static SimplePermissionVO toSimplePermissionVO(Permission permission){
        SimplePermissionVO simplePermissionVO = new SimplePermissionVO();
        simplePermissionVO.setId(permission.getId());
        simplePermissionVO.setName(permission.getName());
        simplePermissionVO.setDescription(permission.getDescription());
        simplePermissionVO.setStatus(permission.getStatus());
        return simplePermissionVO;
    }
}
