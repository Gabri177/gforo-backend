package com.yugao.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.chat.PrivateChatSession;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrivateChatSessionMapper extends BaseMapper<PrivateChatSession> {
}
