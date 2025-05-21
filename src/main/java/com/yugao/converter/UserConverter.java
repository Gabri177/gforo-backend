package com.yugao.converter;

import com.yugao.domain.user.User;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.enums.StatusEnum;
import com.yugao.util.security.PasswordUtil;
import com.yugao.vo.user.SimpleUserVO;
import com.yugao.vo.user.UserInfoVO;
import java.util.Date;

public class UserConverter {

    private static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;

        String[] parts = email.split("@");
        String name = parts[0];
        String domain = parts[1];

        if (name.length() <= 2) {
            return name.charAt(0) + "..." + "@" + domain;
        } else if (name.length() <= 6) {
            return name.substring(0, 1) + "..." + name.substring(name.length() - 1) + "@" + domain;
        } else {
            return name.substring(0, 2) + "..." + name.substring(name.length() - 2) + "@" + domain;
        }
    }


    public static User toDomain(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(PasswordUtil.encode(userRegisterDTO.getPassword()));
        user.setEmail(userRegisterDTO.getEmail());
        user.setHeaderUrl(userRegisterDTO.getHeaderUrl());
        user.setExpPoints(0);
        user.setStatus(StatusEnum.NORMAL);
        user.setCreateTime(new Date());
        user.setNickname(userRegisterDTO.getUsername());
        user.setTitleId(1L);
        return user;
    }

    public static UserInfoVO toUserInfoVO(User domain, Boolean encodeEmail) {
        UserInfoVO vo = new UserInfoVO();
        vo.setId(domain.getId());
        vo.setUsername(domain.getUsername());
        if (encodeEmail) {
            vo.setEmail(maskEmail(domain.getEmail()));
        } else {
            vo.setEmail(domain.getEmail());
        }
        vo.setHeaderUrl(domain.getHeaderUrl());
        vo.setBio(domain.getBio());
        vo.setCreatedAt(domain.getCreateTime());
        vo.setStatus(domain.getStatus());
        vo.setNickname(domain.getNickname());
        return vo;
    }

    /**
     * @param domain
     * @return
     */
    public static SimpleUserVO toSimpleVO(User domain) {
        SimpleUserVO vo = new SimpleUserVO();
        if (domain == null || domain.getStatus() == StatusEnum.DELETED)
            domain = User.createGhostUser();
        if (domain.getStatus() == StatusEnum.DISABLED){
            vo.setUsername(domain.getUsername() + "（Disabled）");
            vo.setNickname(domain.getNickname() + "（Disabled）");
        } else {
            vo.setUsername(domain.getUsername());
            vo.setNickname(domain.getNickname());
        }

        vo.setId(domain.getId());
        vo.setHeaderUrl(domain.getHeaderUrl());

        return vo;
    }
}
