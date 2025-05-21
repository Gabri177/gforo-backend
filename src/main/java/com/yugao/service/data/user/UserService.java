package com.yugao.service.data.user;

import com.yugao.domain.user.User;
import com.yugao.dto.user.UserInfoUpdateDTO;
import com.yugao.enums.StatusEnum;
import com.yugao.vo.statistics.MonthlyUserStatsVO;

import java.util.Date;
import java.util.List;

public interface UserService {

    Long getUserCount();

    Long getUserCountWithLowerLevel(Long userId, Integer level);

    Long getUserCountWithLowerLevel(Long userId, Integer level, String usernameKeyWord);

    User getUserById(Long Id);

    List<User> getUsersByIds(List<Long> ids);

    User getUserByName(String username);

    User getUserByEmail(String email);

    List<User> getUsers(Long id, Integer currentPage, Integer pageSize, Integer curUserLevel);

    List<User> getUsers(Long userId, String usernameKeyWord,
                        Integer currentPage, Integer pageSize, Integer curUserLevel);

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

    Double getMonthGrowthRate();

    List<MonthlyUserStatsVO> getMonthlyRegisterStats();

    void increaseExp(Long userId, Integer exp);

    void decreaseExp(Long userId, Integer exp);
}
