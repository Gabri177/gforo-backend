package com.yugao.converter;

import com.yugao.domain.User;
import com.yugao.dto.UserRegisterDTO;
import com.yugao.util.EncryptedUtil;
import com.yugao.vo.UserInfoVO;

import java.util.Date;
import java.util.UUID;

public class UserConverter {

    public static User toDomain(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(EncryptedUtil.md5(userRegisterDTO.getPassword() + user.getSalt()));
        user.setEmail(userRegisterDTO.getEmail());
        user.setHeaderUrl(userRegisterDTO.getHeaderUrl());
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(EncryptedUtil.generateUUID());
        user.setCreateTime(new Date());
        return user;
    }

    public static UserInfoVO toVO(User domain) {
        UserInfoVO vo = new UserInfoVO();
        vo.setId(domain.getId());
        vo.setUsername(domain.getUsername());
        vo.setEmail(domain.getEmail());
        vo.setHeaderUrl(domain.getHeaderUrl());
        vo.setBio(domain.getBio());
        vo.setCreatedAt(domain.getCreateTime().toString());
        vo.setStatus(domain.getStatus());
        return vo;
    }
}
