package com.zeydie.dlpsystem.server.data.dto;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public record ComputerDTO
        (
                @NonNull UUID computerId,
                @NonNull String name,
                @NonNull String ip,
                boolean active,
                @Nullable LocalDateTime lastStartup,
                @Nullable LocalDateTime lastUpdate
        ) {
}
