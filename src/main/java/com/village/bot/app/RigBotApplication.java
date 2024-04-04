package com.village.bot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.village.bot")
public class RigBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(RigBotApplication.class, args);
    }
}
