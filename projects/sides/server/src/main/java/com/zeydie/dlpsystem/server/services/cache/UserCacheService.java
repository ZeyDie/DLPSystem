package com.zeydie.dlpsystem.server.services.cache;

import com.zeydie.dlpsystem.server.data.entity.UserEntity;
import com.zeydie.dlpsystem.server.repositories.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.zeydie.dlpsystem.api.CaffeineCachePaths.USERS_CACHE;

@Service
@RequiredArgsConstructor
public class UserCacheService {
    private final UserRepository userRepository;

    @Cacheable(value = USERS_CACHE, sync = true)
    public List<UserEntity> findAll() {
        return this.userRepository.findAll();
    }

    @CacheEvict(value = USERS_CACHE, key = "#userId")
    public void evictUsersCache(@NonNull final UUID userId) {
    }

    @CacheEvict(value = USERS_CACHE, allEntries = true)
    public void evictAllCache() {
    }


}
