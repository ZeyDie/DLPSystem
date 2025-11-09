package com.zeydie.dlpsystem.api.v1.computer.request;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public record ComputerUpdateRequest(
        @Nullable String name,
        @Nullable String domain,
        @Nullable String ip,
        long lastStartup
) {
}