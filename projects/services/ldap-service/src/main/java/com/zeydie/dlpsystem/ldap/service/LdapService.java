package com.zeydie.dlpsystem.ldap.service;

import com.zeydie.dlpsystem.api.logger.AppLogger;
import com.zeydie.dlpsystem.api.services.ICacheable;
import com.zeydie.dlpsystem.api.services.ILdapService;
import com.zeydie.dlpsystem.api.services.ILogService;
import com.zeydie.dlpsystem.ldap.configurations.LdapConfig;
import com.zeydie.dlpsystem.ldap.service.cache.LdapCacheService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LdapService implements ILdapService, ICacheable {
    private final AppLogger logger;

    private final ILogService loggerService;

    private final LdapConfig ldapConfig;
    private final LdapCacheService ldapCacheService;

    @PostConstruct
    @Override
    public void initCache() {
        if (this.ldapConfig.isEnabled()) {
            this.logger.info("LdapService initCache");

            @NonNull val computers = this.ldapCacheService.findComputersAll();

            this.logger.info("LdapService cached {}", computers.size());
        }
    }

    @Override
    public void evictAllCache() {
        this.ldapCacheService.evictAllCache();
    }

    @Override
    public boolean isComputerExist(@NonNull final String name) {
        return !this.ldapConfig.isEnabled() || this.ldapCacheService.isComputerByNameExist(name);
    }
}