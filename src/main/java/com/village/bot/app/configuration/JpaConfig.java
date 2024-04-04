package com.village.bot.app.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.village.bot.repositories")
@EntityScan("com.village.bot.entities")
public class JpaConfig {
}
