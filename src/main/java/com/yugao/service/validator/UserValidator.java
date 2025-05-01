package com.yugao.service.validator;

import com.yugao.domain.User;
import com.yugao.dto.user.UserChangePasswordDTO;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.service.base.RedisService;
import com.yugao.service.data.UserService;
import com.yugao.service.data.UserTokenService;
import com.yugao.util.security.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class UserValidator {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserTokenService userTokenService;

    @Value("${change.email-change-interval-limit-days}")
    private Long emailChangeIntervalLimitDays;

    @Value("${change.username-change-interval-limit-days}")
    private Long usernameChangeIntervalLimitDays;

    public User validateExistenceID(Long userId) {
        User userDomain = userService.getUserById(userId);
        if (userDomain == null)
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        return userDomain;
    }

    public User validateExistenceEmail(String email) {
        User existUser = userService.getUserByEmail(email);
        if (existUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return existUser;
    }

    public void validatePasswordChange(User userDomain, UserChangePasswordDTO userChangePasswordDTO) {

        if (userDomain == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (userChangePasswordDTO.getOldPassword().equals(userChangePasswordDTO.getNewPassword())) {
            throw new BusinessException(ResultCode.NEW_PASSWORD_SAME);
        }

        String oldRawPassword = userChangePasswordDTO.getOldPassword();
        String newRawPassword = userChangePasswordDTO.getNewPassword();
        String currentPassword = userDomain.getPassword();
        if (!PasswordUtil.matches(oldRawPassword, currentPassword)) {
            throw new BusinessException(ResultCode.OLD_PASSWORD_INCORRECT);
        }

        if (PasswordUtil.matches(newRawPassword, currentPassword)) {
            throw new BusinessException(ResultCode.NEW_PASSWORD_SAME);
        }
    }

    public User validateUserLogin(UserRegisterDTO dto) {
        User user = userService.getUserByName(dto.getUsername());
        if (user == null) throw new BusinessException(ResultCode.USER_NOT_FOUND);
        if (!PasswordUtil.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_NOT_MATCH);
        }
        return user;
    }

    public void validateIfIsBlocked(User user){
        if (user.getStatus() == 1)
            throw new BusinessException(ResultCode.USER_BLOCKED); ////////////status 值考虑////////////////////
    }

    public void hasPermissionToChangeEmail(Long userId){
        Date lastEmailUpdateTime = userService.getLastEmailUpdateTime(userId);
        if (lastEmailUpdateTime == null)
            return ;
        long diff = ChronoUnit.DAYS.between(
                lastEmailUpdateTime.toInstant(),
                new Date().toInstant());
        if (diff < emailChangeIntervalLimitDays)
            throw new BusinessException(ResultCode.EMAIL_CHANGE_INTERVAL_TOO_SHORT);
    }

    public void hasPermissionToChangeUsername(Long userId){
        Date lastUsernameUpdateTime = userService.getLastUsernameUpdateTime(userId);
        if (lastUsernameUpdateTime == null)
            return ;
        long diff = ChronoUnit.DAYS.between(
                lastUsernameUpdateTime.toInstant(),
                new Date().toInstant());
        if (diff < usernameChangeIntervalLimitDays)
            throw new BusinessException(ResultCode.USERNAME_CHANGE_INTERVAL_TOO_SHORT);
    }


}
