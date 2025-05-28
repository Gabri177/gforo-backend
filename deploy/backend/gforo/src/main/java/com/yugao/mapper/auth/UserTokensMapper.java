package com.yugao.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.auth.UserToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserTokensMapper extends BaseMapper<UserToken> {
}
