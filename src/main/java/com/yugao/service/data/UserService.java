package com.yugao.service.data;

import com.yugao.domain.User;
import com.yugao.dto.user.UserInfoUpdateDTO;
import com.yugao.enums.StatusEnum;

import java.util.Date;
import java.util.List;

public interface UserService {

    Long getUserCount();

    User getUserById(Long Id);

    List<User> getUsersByIds(List<Long> ids);

    User getUserByName(String username);

    User getUserByEmail(String email);

    List<User> getUsers(Long id, Integer currentPage, Integer pageSize, Boolean isAsc);

    boolean updateUser(User user);

    boolean  addUser(User user);

    boolean  updateStatus(Long id, StatusEnum status);

    boolean  updateHeader(Long id, String headerUrl);

    boolean  updateUsername(Long id, String username);

    boolean  updatePassword(Long id, String password);

    boolean  updateEmail(Long id, String email);

    boolean  existsByEmail(String email);

    boolean  existsByUsername(String username);

    boolean updateUserProfile(Long id, UserInfoUpdateDTO userInfoUpdateDTO);

    Date getLastUsernameUpdateTime(Long id);

    Date getLastEmailUpdateTime(Long id);


}
