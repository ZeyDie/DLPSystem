package com.zeydie.dlpsystem.api.v1.computer.reponse;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

@Data
@Builder
public class ComputerResponse {
    private @Nullable String name;
    private boolean active;
    private @Nullable LocalDateTime lastStartup;
    private @Nullable LocalDateTime lastUpdate;
}