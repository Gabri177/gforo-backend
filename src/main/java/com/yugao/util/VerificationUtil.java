package com.yugao.util;

import java.security.SecureRandom;

public class VerificationUtil {

    public static String generateSixNumVerifCode() {

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
