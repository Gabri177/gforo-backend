package com.yugao.service.data.title.impl;

import com.yugao.domain.title.UserExpLog;
import com.yugao.mapper.title.UserExpLogMapper;
import com.yugao.service.data.title.UserExpLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserExpLogServiceImpl implements UserExpLogService {

    private final UserExpLogMapper userExpLogMapper;

    @Override
    public void addExpLog(UserExpLog userExpLog) {

        userExpLogMapper.insert(userExpLog);
    }
}
