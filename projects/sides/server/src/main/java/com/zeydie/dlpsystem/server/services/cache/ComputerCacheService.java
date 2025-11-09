package com.zeydie.dlpsystem.server.services.cache;

import com.zeydie.dlpsystem.server.data.entity.ComputerEntity;
import com.zeydie.dlpsystem.server.repositories.ComputerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.zeydie.dlpsystem.api.CaffeineCachePaths.COMPUTERS_CACHE;

@Service
@RequiredArgsConstructor
public class ComputerCacheService {
    private final ComputerRepository computerRepository;

    @Cacheable(value = COMPUTERS_CACHE, sync = true)
    public List<ComputerEntity> findAll() {
        return this.computerRepository.findAll();
    }

    @Cacheable(value = COMPUTERS_CACHE, key = "#computerId")
    public @Nullable ComputerEntity getComputerById(@NonNull final UUID computerId) {
        return this.computerRepository.findByComputerId(computerId).orElse(null);
    }

    @Cacheable(value = COMPUTERS_CACHE, key = "#name")
    public @Nullable ComputerEntity getComputerByName(@NonNull final String name) {
        return this.computerRepository.findByName(name).orElse(null);
    }

    public @Nullable ComputerEntity createComputer(@NonNull final String name, @NonNull final String ip) {
        return this.save(new ComputerEntity(name, ip));
    }

    @CachePut(value = COMPUTERS_CACHE, key = "#computerId")
    public @NotNull ComputerEntity save(@NonNull final ComputerEntity computerEntity) {
        @NonNull val entity = this.computerRepository.save(computerEntity);

        this.evictComputersCacheByName(entity.getName());

        return entity;
    }

    @CacheEvict(value = COMPUTERS_CACHE, key = "#computerId")
    public void evictComputersCache(@NonNull final UUID computerId) {
    }

    @CacheEvict(value = COMPUTERS_CACHE, key = "#name")
    public void evictComputersCacheByName(@NonNull final String name) {
    }

    @CacheEvict(value = COMPUTERS_CACHE, allEntries = true)
    public void evictAllCache() {
    }
}