package com.zeydie.dlpsystem.server.data.dto;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public record UserDTO
        (
                @NonNull String username,
                @NonNull String label,
                @Nullable LocalDateTime lastLogin
        ){
}
