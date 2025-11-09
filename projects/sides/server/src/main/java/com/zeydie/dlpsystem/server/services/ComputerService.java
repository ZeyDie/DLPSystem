package com.zeydie.dlpsystem.server.services;

import com.zeydie.dlpsystem.api.logger.AppLogger;
import com.zeydie.dlpsystem.api.services.ICacheable;
import com.zeydie.dlpsystem.api.services.IComputerService;
import com.zeydie.dlpsystem.api.services.ILdapService;
import com.zeydie.dlpsystem.api.services.ILogService;
import com.zeydie.dlpsystem.server.data.ComputerMapper;
import com.zeydie.dlpsystem.server.data.dto.ComputerDTO;
import com.zeydie.dlpsystem.server.data.entity.ComputerEntity;
import com.zeydie.dlpsystem.server.services.cache.ComputerCacheService;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComputerService implements IComputerService, ICacheable {
    private final AppLogger logger;

    private final ILogService loggerService;
    private final ILdapService ldapService;

    private final ComputerCacheService computerCacheService;

    @PostConstruct
    @Override
    public void initCache() {
        this.logger.info("ComputerService initCache");

        @NonNull val computers = this.computerCacheService.findAll();

        this.logger.info("ComputerService cached {}", computers.size());
    }

    @Override
    public void evictAllCache() {
        this.computerCacheService.evictAllCache();
    }

    @Override
    public @Nullable ComputerDTO getComputerById(@NonNull final UUID computerId) {
        return ComputerMapper.INSTANCE.toDTO(this.computerCacheService.getComputerById(computerId));
    }

    @Override
    public @Nullable ComputerDTO getComputerByName(@NonNull final String name) {
        return ComputerMapper.INSTANCE.toDTO(this.computerCacheService.getComputerByName(name));
    }

    @Override
    public @Nullable ComputerDTO updateComputer(
            @NonNull final String name,
            @NonNull final String ip,
            @NonNull final String domain,
            final long lastStartup
    ) {
        @Nullable var computerEntity = this.computerCacheService.getComputerByName(name);

        if (computerEntity == null) {
            computerEntity = this.createComputer(name, ip);

            if (computerEntity == null) {
                @NonNull val message = "Bad computer trying to update " + name + " with ip " + ip;

                this.logger.info(message);
                this.loggerService.log(message);
            }
        }

        computerEntity.setIp(ip);
        computerEntity.setActive(true);
        computerEntity.setLastStartup(
                Instant.ofEpochMilli(lastStartup)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
        );
        computerEntity.setLastUpdate(LocalDateTime.now());

        computerEntity = this.computerCacheService.save(computerEntity);

        @NonNull val message = "Computer status updated " + name + " with ip " + ip;

        this.logger.info(message);
        this.loggerService.logComputer(
                computerEntity.getComputerId(),
                computerEntity.getIp(),
                message
        );

        return ComputerMapper.INSTANCE.toDTO(computerEntity);

    }

    @Override
    public @Nullable ComputerEntity createComputer(@NonNull final String name, @NonNull final String ip) {
        if (this.ldapService.isComputerExist(name)) {
            @NonNull var message = "Creating computer " + name + " with ip " + ip;

            this.logger.info(message);
            this.loggerService.log(message);

            @Nullable val computerEntity = this.computerCacheService.createComputer(name, ip);

            if (computerEntity != null) {
                message = "Computer was created " + name + " with ip " + ip;

                this.logger.info(message);
                this.loggerService.logComputer(
                        computerEntity.getComputerId(),
                        computerEntity.getIp(),
                        message
                );
            } else {
                message = "Computer was not created " + name + " with ip " + ip;

                this.logger.info(message);
                this.loggerService.log(message);
            }

            return computerEntity;
        }

        return null;
    }

    public void updateStatuses() {
        this.computerCacheService.findAll()
                .stream()
                .filter(computerEntity -> computerEntity.isActive())
                .filter(computerEntity -> computerEntity.getLastUpdate() != null)
                .filter(computerEntity -> Duration.between(computerEntity.getLastUpdate(), LocalDateTime.now()).toMinutes() >= 4)
                .forEach(computerEntity -> {
                            computerEntity.setActive(false);

                            this.computerCacheService.save(computerEntity);
                        }
                );
    }
}