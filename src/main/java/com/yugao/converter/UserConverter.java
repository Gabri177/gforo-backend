package com.yugao.converter;

import com.yugao.domain.User;
import com.yugao.dto.UserDTO;
import com.yugao.util.EncryptedUtil;
import com.yugao.vo.UserVO;

import java.util.Date;
import java.util.UUID;

public class UserConverter {

    public static User toDomain(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(EncryptedUtil.md5(userDTO.getPassword() + user.getSalt()));
        user.setEmail(userDTO.getEmail());
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(EncryptedUtil.generateUUID());
        user.setCreateTime(new Date());
        return user;
    }

    public static UserVO toVO(User domain) {
        UserVO vo = new UserVO();
        vo.setId(domain.getId());
        vo.setUsername(domain.getUsername());
        vo.setEmail(domain.getEmail());
        vo.setHeaderUrl(domain.getHeaderUrl());
        return vo;
    }
}
