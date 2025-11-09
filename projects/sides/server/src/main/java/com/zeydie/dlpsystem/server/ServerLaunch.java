package com.zeydie.dlpsystem.server;

import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * ♥
 */
@ComponentScan("com.zeydie.dlpsystem")
@EnableJpaRepositories("com.zeydie.dlpsystem")
@EntityScan("com.zeydie.dlpsystem")
@EnableAsync
@EnableWebSecurity
@EnableScheduling
@AutoConfiguration
@SpringBootApplication
public class ServerLaunch {
    public static void main(@Nullable final String[] args) {
        SpringApplication.run(ServerLaunch.class, args);
    }
}