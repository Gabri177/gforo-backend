package com.yugao.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.chat.PrivateMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrivateMessageMapper extends BaseMapper<PrivateMessage> {
}
