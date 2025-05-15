package com.yugao.service.validator;

import com.yugao.domain.user.User;
import com.yugao.dto.user.UserChangePasswordDTO;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.enums.StatusEnum;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.service.base.RedisService;
import com.yugao.service.data.UserService;
import com.yugao.service.data.UserTokenService;
import com.yugao.service.handler.UserHandler;
import com.yugao.util.security.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserService userService;
    private final UserHandler userHandler;

    @Value("${change.email-change-interval-limit-days}")
    private Long emailChangeIntervalLimitDays;

    @Value("${change.username-change-interval-limit-days}")
    private Long usernameChangeIntervalLimitDays;

    public User validateUserIdExists(Long userId) {
        // 111111111111111
        User userDomain = userHandler.getUser(userId);
        if (userDomain == null)
            throw new BusinessException(ResultCodeEnum.USER_NOT_FOUND);
        return userDomain;
    }

    public User validateEmailExists(String email) {
        User existUser = userService.getUserByEmail(email);
        if (existUser == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_FOUND);
        }
        return existUser;
    }

    public void validatePasswordChange(User userDomain, UserChangePasswordDTO userChangePasswordDTO) {

        if (userDomain == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_FOUND);
        }

        if (userChangePasswordDTO.getOldPassword().equals(userChangePasswordDTO.getNewPassword())) {
            throw new BusinessException(ResultCodeEnum.NEW_PASSWORD_SAME);
        }

        String oldRawPassword = userChangePasswordDTO.getOldPassword();
        String newRawPassword = userChangePasswordDTO.getNewPassword();
        String currentPassword = userDomain.getPassword();
        if (!PasswordUtil.matches(oldRawPassword, currentPassword)) {
            throw new BusinessException(ResultCodeEnum.OLD_PASSWORD_INCORRECT);
        }

        if (PasswordUtil.matches(newRawPassword, currentPassword)) {
            throw new BusinessException(ResultCodeEnum.NEW_PASSWORD_SAME);
        }
    }

    public User validateLoginCredentials(UserRegisterDTO dto) {
        User user = userService.getUserByName(dto.getUsername());
        if (user == null) throw new BusinessException(ResultCodeEnum.USER_NOT_FOUND);
        if (!PasswordUtil.matches(dto.getPassword(), user.getPassword()))
            throw new BusinessException(ResultCodeEnum.PASSWORD_NOT_MATCH);
        if (user.getStatus() != StatusEnum.NORMAL)
            throw new BusinessException(ResultCodeEnum.USER_ACCOUNT_ABNORMAL);

        return user;
    }

    public void validateIfIsDeleted(User user){
        if (user.getStatus() == StatusEnum.DELETED)
            throw new BusinessException(ResultCodeEnum.USER_DELETED);
    }

    public void validateEmailChangeInterval(Long userId){
        Date lastEmailUpdateTime = userService.getLastEmailUpdateTime(userId);
        if (lastEmailUpdateTime == null)
            return ;
        long diff = ChronoUnit.DAYS.between(
                lastEmailUpdateTime.toInstant(),
                new Date().toInstant());
        if (diff < emailChangeIntervalLimitDays)
            throw new BusinessException(ResultCodeEnum.EMAIL_CHANGE_INTERVAL_TOO_SHORT);
    }

    public void validateUsernameChangeInterval(Long userId){
        Date lastUsernameUpdateTime = userService.getLastUsernameUpdateTime(userId);
        if (lastUsernameUpdateTime == null)
            return ;
        long diff = ChronoUnit.DAYS.between(
                lastUsernameUpdateTime.toInstant(),
                new Date().toInstant());
        if (diff < usernameChangeIntervalLimitDays)
            throw new BusinessException(ResultCodeEnum.USERNAME_CHANGE_INTERVAL_TOO_SHORT);
    }


}
