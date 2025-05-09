package com.yugao.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.user.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
