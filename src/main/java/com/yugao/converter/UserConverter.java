package com.yugao.converter;

import com.yugao.domain.User;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.util.common.EncryptedUtil;
import com.yugao.util.security.PasswordUtil;
import com.yugao.vo.user.SimpleUserVO;
import com.yugao.vo.user.UserInfoVO;

import java.util.Date;

public class UserConverter {

    public static User toDomain(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(PasswordUtil.encode(userRegisterDTO.getPassword()));
        user.setEmail(userRegisterDTO.getEmail());
        user.setHeaderUrl(userRegisterDTO.getHeaderUrl());
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(EncryptedUtil.generateUUID());
        user.setCreateTime(new Date());
        user.setNickname(userRegisterDTO.getUsername());
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
        vo.setNickname(domain.getNickname());
        return vo;
    }

    /**
     * !!!!!!!!!!! 对于未知用户的处理 可能要修改
     * @param domain
     * @return
     */
    public static SimpleUserVO toSimpleVO(User domain) {
        SimpleUserVO vo = new SimpleUserVO();
        if (domain == null) {
            vo.setId(-1L);
            vo.setUsername("UnknownUser");
            vo.setNickname("Unknown");
            vo.setHeaderUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnpG1ZOxWU7_lCGM2Szc9IUKX9s0vkUDGnng&s");
        } else {
            vo.setId(domain.getId());
            vo.setUsername(domain.getUsername());
            vo.setNickname(domain.getNickname());
            vo.setHeaderUrl(domain.getHeaderUrl());
        }
        return vo;
    }
}
