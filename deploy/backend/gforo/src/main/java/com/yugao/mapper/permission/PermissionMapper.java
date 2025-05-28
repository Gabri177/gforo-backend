package com.yugao.mapper.permission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.permission.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
