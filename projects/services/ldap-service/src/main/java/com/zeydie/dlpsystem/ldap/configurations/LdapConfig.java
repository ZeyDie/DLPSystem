package com.zeydie.dlpsystem.ldap.configurations;

import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import java.util.Locale;

@Configuration
@EnableLdapRepositories
public class LdapConfig {
    @Getter
    @Value("${app.ldap.enabled}")
    private boolean enabled;
    @Value("${app.ldap.url}")
    private String url;
    @Value("${app.ldap.base}")
    private String base;
    @Value("${app.ldap.username}")
    private String username;
    @Value("${app.ldap.password}")
    private String password;

    @Value("${app.ldap.domain}")
    private String domain;
    @Value("${app.ldap.path.users}")
    private String usersPath;

    @Bean
    public @NotNull LdapContextSource contextSource() {
        @NonNull val contextSource = new LdapContextSource();

        contextSource.setUrl(this.url);
        contextSource.setBase(this.base);
        contextSource.setUserDn(this.username);
        contextSource.setPassword(this.password);

        return contextSource;
    }

    @Bean
    public @NotNull LdapTemplate ldapTemplate() {
        return new LdapTemplate(this.contextSource());
    }
}