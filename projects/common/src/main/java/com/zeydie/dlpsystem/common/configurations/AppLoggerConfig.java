package com.zeydie.dlpsystem.common.configurations;

import com.zeydie.dlpsystem.api.logger.AppLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppLoggerConfig {
    @Bean
    public AppLogger appLogger() {
        return new AppLogger();
    }
}