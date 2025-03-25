package com.yugao.util;

import org.springframework.util.DigestUtils;
import java.util.UUID;

public class EncryptedUtil {

    // generate random string
    public static String generateUUID() {

        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
