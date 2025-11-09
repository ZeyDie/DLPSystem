package com.zeydie.dlpsystem.api.logger;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppLogger {
    public void info(@NonNull final String message) {
        log.info(message);
    }

    public void info(@NonNull final Object object) {
        this.info("{}", object);
    }

    public void info(@NonNull final String message, @Nullable final Object... arguments) {
        log.info(message, arguments);
    }

    public void warn(@NonNull final String message) {
        log.warn(message);
    }

    public void warn(@NonNull final Object object) {
        this.warn("{}", object);
    }

    public void warn(@NonNull final String message, @Nullable final Object... arguments) {
        log.warn(message, arguments);
    }

    public void error(@NonNull final String message) {
        log.error(message);
    }

    public void error(@NonNull final Object object) {
        this.error("{}", object);
    }

    public void error(@NonNull final String message, @Nullable final Object... arguments) {
        log.error(message, arguments);
    }

    public void debug(@NonNull final String message) {
        log.debug(message);
    }

    public void debug(@NonNull final Object object) {
        this.debug("{}", object);
    }

    public void debug(@NonNull final String message, @Nullable final Object... arguments) {
        log.debug(message, arguments);
    }

    public void trace(@NonNull final String message) {
        log.trace(message);
    }

    public void trace(@NonNull final String message, @Nullable final Object... arguments) {
        log.trace(message, arguments);
    }
}