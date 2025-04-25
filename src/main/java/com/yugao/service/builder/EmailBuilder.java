package com.yugao.service.builder;

import com.yugao.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailBuilder {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${resetPassword.sixDigVerifyCodeExpireTimeMinutes}")
    private long resetPasswordSixDigVerifyCodeExpireTimeMinutes;

    public String buildActivationLink(User user) {
        return frontendUrl + "/register/" + user.getId() + "/" + user.getActivationCode();
    }

    public String buildEmailVerifyHtml(String username, String link) {
        return "<p>Hi " + username + ",</p>" +
                "<p>Please click the link below to verify your email address:</p>" +
                "<p><a href='" + link + "' style='color: #4A90E2;'>Verify Email</a></p>" +
                "<p>If the button doesn't work, copy this link into your browser:</p>" +
                "<p>" + link + "</p>";
    }

    public String buildSixCodeVerifyHtml(String sixDigVerifyCode) {

        String htmlContent = "<p>Your six-digit verification code is: " +
                "<span style='color:red; font-size:20px; font-weight:bold'>" +
                sixDigVerifyCode +
                "</span>.</p>" +
                "<p>This code will expire in " +
                "<span style='color:orange; font-weight:bold'>" +
                resetPasswordSixDigVerifyCodeExpireTimeMinutes +
                "</span> minutes.</p>";
        return htmlContent;
    }
}
