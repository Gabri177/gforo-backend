package com.yugao.util;

import org.springframework.util.DigestUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;

public class ForoUtil {

    // generate random string
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5 encryption
    public static String md5(String key) {
//        if (key == null || StringUtils.isBlank(key)) {
//            return null;
//        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
