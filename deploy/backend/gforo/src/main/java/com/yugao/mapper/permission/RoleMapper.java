package com.yugao.mapper.permission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.permission.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
