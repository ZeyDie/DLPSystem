package com.zeydie.dlpsystem.server.services.schedulers;

import com.zeydie.dlpsystem.server.services.ComputerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ComputerSchedulerService {
    private final ComputerService computerService;

    @Async
    @Scheduled(initialDelay = 0, fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void updateStatuses() {
        this.computerService.updateStatuses();
    }
}