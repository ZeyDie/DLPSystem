package com.zeydie.dlpsystem.ldap.service.cache;

import com.google.common.collect.Lists;
import com.zeydie.dlpsystem.ldap.configurations.LdapConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.zeydie.dlpsystem.ldap.paths.CaffeineCachePaths.LDAP_COMPUTERS_CACHE;

@Service
@RequiredArgsConstructor
public class LdapCacheService {
    @Value("${app.ldap.path.pc}")
    private List<String> ldapPathPC;

    private final LdapConfig ldapConfig;
    private final LdapTemplate ldapTemplate;

    private final @NotNull AndFilter computersFilter = new AndFilter()
            .and(new EqualsFilter("objectClass", "computer"));

    @Cacheable(value = LDAP_COMPUTERS_CACHE, sync = true)
    public @NotNull List<String> findComputersAll() {
        @NonNull val list = Lists.<String>newArrayList();

        if (this.ldapConfig.isEnabled())
            this.ldapPathPC.stream()
                    .forEach(
                            path -> {
                                list.addAll(
                                        this.ldapTemplate.search(
                                                path,
                                                this.computersFilter.encode(),
                                                (AttributesMapper<String>) attributes -> attributes.get("cn").get().toString()
                                        )
                                );
                            }
                    );

        return list;
    }

    @Cacheable(value = LDAP_COMPUTERS_CACHE, key = "#name")
    public boolean isComputerByNameExist(@NonNull final String name) {
        return this.findComputersAll().contains(name);
    }

    @CacheEvict(value = LDAP_COMPUTERS_CACHE, key = "#name")
    public void evictComputerCache(@NonNull final String name) {
    }

    @CacheEvict(value = LDAP_COMPUTERS_CACHE, allEntries = true)
    public void evictAllCache() {
    }
}