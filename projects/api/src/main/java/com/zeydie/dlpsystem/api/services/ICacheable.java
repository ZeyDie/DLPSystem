package com.zeydie.dlpsystem.api.services;

import jakarta.annotation.PostConstruct;

public interface ICacheable {
    @PostConstruct
    void initCache();

    void evictAllCache();
}
