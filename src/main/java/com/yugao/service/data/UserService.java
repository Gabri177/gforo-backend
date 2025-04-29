package com.yugao.service.data;

import com.yugao.domain.User;
import com.yugao.dto.user.UserInfoUpdateDTO;

import java.util.List;

public interface UserService {


    User getUserById(Long Id);

    List<User> getUsersByIds(List<Long> ids);

    User getUserByName(String username);

    User getUserByEmail(String email);

    boolean updateUser(User user);

    boolean  addUser(User user);

    boolean  updateStatus(Long id, int status);

    boolean  updateHeader(Long id, String headerUrl);

    boolean  updatePassword(Long id, String password);

    boolean  updateEmail(Long id, String email);

    boolean  existsByEmail(String email);

    boolean  existsByUsername(String username);

    void updateUserProfile(Long id, UserInfoUpdateDTO userInfoUpdateDTO);


}
