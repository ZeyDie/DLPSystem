package com.zeydie.dlpsystem.server.repositories;

import com.zeydie.dlpsystem.server.data.entity.ComputerEntity;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComputerRepository extends JpaRepository<ComputerEntity, Long> {
    @NotNull Optional<ComputerEntity> findByComputerId(@NonNull final UUID computerId);

    @NotNull Optional<ComputerEntity> findByName(@NonNull final String name);
}