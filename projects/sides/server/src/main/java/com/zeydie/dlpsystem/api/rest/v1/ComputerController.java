package com.zeydie.dlpsystem.api.rest.v1;

import com.zeydie.dlpsystem.api.logger.AppLogger;
import com.zeydie.dlpsystem.api.v1.computer.endpoint.IComputerEndpoint;
import com.zeydie.dlpsystem.api.v1.computer.reponse.ComputerResponse;
import com.zeydie.dlpsystem.api.v1.computer.request.ComputerUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/computer")
@RequiredArgsConstructor
public class ComputerController {
    private final AppLogger logger;

    private final IComputerEndpoint computerEndpoint;

    private final @NonNull ResponseEntity<ComputerResponse> notFound = ResponseEntity.notFound().build();
    private final @NonNull ResponseEntity<ComputerResponse> badRequest = ResponseEntity.badRequest().build();

    @GetMapping("/id/{computerId}")
    public @NotNull ResponseEntity<ComputerResponse> getComputerById(
            @NonNull final HttpServletRequest httpServletRequest,
            @PathVariable("computerId") @NotNull final UUID computerId
    ) {
        this.logger.info(
                "/computerId -> {} => {}",
                httpServletRequest.getRemoteAddr(),
                computerId
        );

        try {
            @Nullable val computer = this.computerEndpoint.getComputerById(httpServletRequest, computerId);

            if (computer != null)
                return ResponseEntity.ok(computer);

            return this.notFound;
        } catch (final Exception exception) {
            return this.badRequest;
        }
    }

    @GetMapping("/name/{name}")
    public @NotNull ResponseEntity<ComputerResponse> getComputerByName(
            @NonNull final HttpServletRequest httpServletRequest,
            @PathVariable("name") @NonNull final String name
    ) {
        this.logger.info(
                "/name -> {} => {}",
                httpServletRequest.getRemoteAddr(),
                name
        );

        try {
            @Nullable val computer = this.computerEndpoint.getComputerByName(httpServletRequest, name);

            if (computer != null)
                return ResponseEntity.ok(computer);

            return this.notFound;
        } catch (final Exception exception) {
            return this.badRequest;
        }
    }

    @Async
    @PutMapping("/status")
    public @NotNull CompletableFuture<ResponseEntity<ComputerResponse>> updateComputer(
            @NonNull final HttpServletRequest httpServletRequest,
            @RequestBody @NotNull final ComputerUpdateRequest request
    ) {
        return CompletableFuture.supplyAsync(
                () -> {
                    this.logger.info(
                            "/status -> {} => {}",
                            httpServletRequest.getRemoteAddr(),
                            request
                    );

                    try {
                        @Nullable val computer = this.computerEndpoint.updateComputer(httpServletRequest, request);

                        if (computer != null)
                            return ResponseEntity.ok(computer);

                        return this.notFound;
                    } catch (final Exception exception) {
                        return this.badRequest;
                    }
                }
        );
    }
}