package com.yugao.util;

import com.yugao.util.mail.MailClientUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailTest {

    @Autowired
    private MailClientUtil mailClient;

    @Test
    public void testSendMail() {
        mailClient.sendMail("forestcat177@gmail.com", "Test", "Hello");
    }
}
