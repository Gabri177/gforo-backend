package com.yugao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.UserToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserTokensMapper extends BaseMapper<UserToken> {
}
