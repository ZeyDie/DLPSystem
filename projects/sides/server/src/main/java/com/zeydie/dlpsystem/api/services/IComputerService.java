package com.zeydie.dlpsystem.api.services;

import com.zeydie.dlpsystem.server.data.dto.ComputerDTO;
import com.zeydie.dlpsystem.server.data.entity.ComputerEntity;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IComputerService {
    @Nullable ComputerDTO getComputerById(@NonNull final UUID computerId);

    @Nullable ComputerDTO getComputerByName(@NonNull final String name);

    @Nullable ComputerDTO updateComputer(
            @NonNull final String name,
            @NonNull final String ip,
            @NonNull final String domain,
            final long lastStartup
    );

    @Nullable ComputerEntity createComputer(
            @NonNull final String name,
            @NonNull final String ip
    );
}