package com.zeydie.dlpsystem.server.services;

import com.zeydie.dlpsystem.api.logger.AppLogger;
import com.zeydie.dlpsystem.api.services.ICacheable;
import com.zeydie.dlpsystem.api.services.ILogService;
import com.zeydie.dlpsystem.api.services.IUserService;
import com.zeydie.dlpsystem.api.v1.computer.request.UserAuthRequest;
import com.zeydie.dlpsystem.server.services.cache.UserCacheService;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements ICacheable, IUserService {
    private final AppLogger logger;

    private final ILogService loggerService;

    private final UserCacheService userCacheService;

    @PostConstruct
    @Override
    public void initCache() {
        this.logger.info("UserService initCache");

        @NonNull val users = this.userCacheService.findAll();

        this.logger.info("UserService cached {}", users.size());
    }

    @Override
    public void evictAllCache() {
        this.userCacheService.evictAllCache();
    }

    @Override
    public boolean authUser(@NonNull final String login, @NonNull final UserAuthRequest.AuthType type) {
        @NonNull val message = "User " + login + " with action " + type.name();

        this.logger.info(message);
        this.loggerService.log(message);

        return true;
    }
}