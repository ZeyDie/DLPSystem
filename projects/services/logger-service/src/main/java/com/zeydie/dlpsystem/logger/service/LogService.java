package com.zeydie.dlpsystem.logger.service;

import com.zeydie.dlpsystem.api.services.ILogService;
import com.zeydie.dlpsystem.logger.entity.ComputerLogEntity;
import com.zeydie.dlpsystem.logger.entity.LogEntity;
import com.zeydie.dlpsystem.logger.entity.UserLogEntity;
import com.zeydie.dlpsystem.logger.repository.ComputerLogRepository;
import com.zeydie.dlpsystem.logger.repository.LogRepository;
import com.zeydie.dlpsystem.logger.repository.UserLogRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogService implements ILogService {
    private final LogRepository logRepository;
    private final UserLogRepository userLogRepository;
    private final ComputerLogRepository computerLogRepository;

    @Override
    public void logUser(
            @NonNull final UUID userId,
            @NonNull @NotBlank final String ip,
            @NonNull @NotBlank final String message
    ) {
        this.userLogRepository.save(
                new UserLogEntity(
                        userId,
                        ip,
                        message
                )
        );
    }

    @Override
    public void logComputer(
            @NonNull final UUID computerId,
            @NonNull @NotBlank final String ip,
            @NonNull @NotBlank final String message
    ) {
        this.computerLogRepository.save(
                new ComputerLogEntity(
                        computerId,
                        ip,
                        message
                )
        );
    }

    @Override
    public void log(@NonNull @NotBlank final String message) {
        this.logRepository.save(new LogEntity(message));
    }
}