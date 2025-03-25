package com.yugao.service;

import com.yugao.domain.User;
import com.yugao.dto.UserInfoUpdateDTO;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {


    User getUserById(Long Id);

    User getUserByName(String username);

    User getUserByEmail(String email);

    boolean updateUser(User user);

    boolean  addUser(User user);

    boolean  updateStatus(Long id, int status);

    boolean  updateHeader(Long id, String headerUrl);

    boolean  updatePassword(Long id, String password);

    boolean  updateEmail(Long id, String email);

    void updateUserProfile(Long id, UserInfoUpdateDTO userInfoUpdateDTO);


}
