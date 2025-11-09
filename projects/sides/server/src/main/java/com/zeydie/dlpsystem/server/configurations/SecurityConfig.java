package com.zeydie.dlpsystem.server.configurations;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Profile("prod")
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${app.cors.enabled}")
    private boolean corsEnabled;
    @Value("${app.cors.allowed.credentials}")
    private boolean allowCredentials;
    @Value("${app.cors.allowed.origins}")
    private String allowedOrigins;
    @Value("${app.cors.allowed.headers}")
    private String allowedHeaders;
    @Value("${app.cors.allowed.methods}")
    private String allowedMethods;

    @Value("${app.csrf.enabled}")
    private boolean csrfEnabled;

    @Bean
    public @NotNull SecurityFilterChain securityFilterChain(@NonNull final HttpSecurity http) throws Exception {
        return http.cors(
                        cors -> {
                            if (!this.corsEnabled)
                                cors.disable();
                            else
                                cors.configurationSource(
                                        request -> {
                                            @NotNull val corsConfig = new CorsConfiguration();

                                            if (this.allowedOrigins != null && !this.allowedOrigins.isEmpty())
                                                corsConfig.setAllowedOrigins(Arrays.asList(this.allowedOrigins.split("\\s*,\\s*")));

                                            if (this.allowedMethods != null && !this.allowedMethods.isEmpty())
                                                corsConfig.setAllowedMethods(Arrays.asList(this.allowedMethods.split("\\s*,\\s*")));

                                            if (this.allowedHeaders != null && !this.allowedHeaders.isEmpty())
                                                corsConfig.setAllowedHeaders(Arrays.asList(this.allowedHeaders.split("\\s*,\\s*")));

                                            corsConfig.setAllowCredentials(this.allowCredentials);

                                            return corsConfig;
                                        }
                                );
                        }
                )
                .csrf(
                        csrf -> {
                            if (!this.csrfEnabled)
                                csrf.disable();
                        }
                )
                .authorizeHttpRequests(
                        auth -> auth//.requestMatchers("/api/v1/authenticate").permitAll()
                                .requestMatchers("/api/v1/computer/**").permitAll()
                                .requestMatchers("/api/v1/user/**").permitAll()
                                .anyRequest().permitAll()
                )
                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}