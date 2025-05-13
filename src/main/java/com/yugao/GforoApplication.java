package com.yugao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GforoApplication {

    public static void main(String[] args) {

        try {
            SpringApplication.run(GforoApplication.class, args);
        } catch (Exception e) {
            System.err.println("❌ 启动失败: " + e.getMessage());
            e.printStackTrace();  // 打印完整异常堆栈
        }
    }

}
