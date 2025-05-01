package com.yugao.service.builder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailBuilder {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${captcha.sixDigVerifyCodeExpireTimeMinutes}")
    private long sixDigVerifyCodeExpireTimeMinutes;

    public String buildActivationLink(String email, String code) {
        return frontendUrl + "/register/" + email + "/" + code;
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
                sixDigVerifyCodeExpireTimeMinutes +
                "</span> minutes.</p>";
        return htmlContent;
    }

    public String buildSixCodeVerifyHtml(String sixDigVerifyCode, String link) {

        String htmlContent = "<p>Your six-digit verification code is: " +
                "<span style='color:red; font-size:20px; font-weight:bold'>" +
                sixDigVerifyCode +
                "</span>.</p>" +
                "<p>This code will expire in " +
                "<span style='color:orange; font-weight:bold'>" +
                sixDigVerifyCodeExpireTimeMinutes +
                "</span> minutes.</p>";
        if (link != null) {
            htmlContent += "<p>You can also click the link to re-fill your verification code:</p>" +
                    "<p><a href='" + link + "' style='color: #4A90E2;'>Activate Account</a></p>" +
                    "<p>If the button doesn't work, copy this link into your browser:</p>" +
                    "<p>" + link + "</p>";
        }
        return htmlContent;
    }
}
