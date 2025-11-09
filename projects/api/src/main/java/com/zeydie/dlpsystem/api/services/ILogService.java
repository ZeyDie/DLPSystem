package com.zeydie.dlpsystem.api.services;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

import java.util.UUID;

public interface ILogService {
    void logUser(
            @NonNull final UUID userId,
            @NonNull @NotBlank final String ip,
            @NonNull @NotBlank final String message
    );

    void logComputer(
            @NonNull final UUID computerId,
            @NonNull @NotBlank final String ip,
            @NonNull @NotBlank final String message
    );

    void log(@NonNull @NotBlank final String message);
}