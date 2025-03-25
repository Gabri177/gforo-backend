package com.yugao.util.common;

import java.util.UUID;

public class EncryptedUtil {

    // generate random string
    public static String generateUUID() {

        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
