package com.zeydie.dlpsystem.common.configurations;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CaffeineCacheConfig {
    @Bean
    public @NotNull Caffeine<Object, Object> caffeine() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofHours(1))
                .recordStats()
                .weakKeys();
    }

    @Bean
    public @NotNull CacheManager cacheManager() {
        @NotNull val caffeine = this.caffeine();
        @NotNull val cacheManager = new CaffeineCacheManager();

        cacheManager.setCaffeine(caffeine);

        return cacheManager;
    }
}