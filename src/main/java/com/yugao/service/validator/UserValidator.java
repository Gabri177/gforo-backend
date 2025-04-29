package com.yugao.service.validator;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.User;
import com.yugao.dto.user.UserChangePasswordDTO;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.dto.auth.UserVerifyEmailDTO;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.service.base.RedisService;
import com.yugao.service.data.UserService;
import com.yugao.service.data.UserTokenService;
import com.yugao.util.captcha.VerificationUtil;
import com.yugao.util.security.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserTokenService userTokenService;

    public User validateExistenceID(Long userId) {
        User userDomain = userService.getUserById(userId);
        if (userDomain == null)
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        return userDomain;
    }

    public User validateExistenceName(String username) {
        User existUser = userService.getUserByName(username);
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

    public User validateUsernameAndEmail(String username, String email) {
        User existUser = userService.getUserByName(username);
        if (existUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if (!existUser.getEmail().equals(email)) {
            throw new BusinessException(ResultCode.EMAIL_NOT_MATCH);
        }
        return existUser;
    }

    public String generateAndCacheSixDigitCode(String scene, String symbol){
        // 生成六位数验证码
        String sixDigVerifyCode = VerificationUtil.generateSixNumVerifCode();

        // 存储到redis中
        redisService.setSigDigitCodeByMinutes(
                scene,
                symbol,
                sixDigVerifyCode
        );

        return sixDigVerifyCode;
    }

    public void verifySixDigitCode(String scene, String symbol, String code) {
        boolean res = redisService.verifySigDigitCode(
                scene,
                symbol,
                code
        );
        if (!res) {
            throw new BusinessException(ResultCode.SIX_DIGIT_CODE_NOT_MATCH);
        }

        // 删除redis中存储的验证码
        redisService.deleteSigDigitCode(scene, code);
        // 设置一个标志位 用来表示用户已经验证过验证码
        // 后续应该可以删除
        redisService.setVerifiedSigDigitCodeByMinutes(scene, symbol);
    }

    public void validateVerifiedCodeFlag(String username){
        boolean res = redisService.verifyVerifiedSigDigitCode(
                RedisKeyConstants.FORGET_PASSWORD,
                username
        );
        if (!res) {
            throw new BusinessException(ResultCode.SIX_DIGIT_CODE_NOT_MATCH);
        }

        // 删除redis中存储的验证码
        redisService.deleteVerifiedSigDigitCode(RedisKeyConstants.FORGET_PASSWORD, username);
    }

    public void validateIfIsBlocked(User user){
        if (user.getStatus() == 2)
            throw new BusinessException(ResultCode.USER_BLOCKED);
    }


}
