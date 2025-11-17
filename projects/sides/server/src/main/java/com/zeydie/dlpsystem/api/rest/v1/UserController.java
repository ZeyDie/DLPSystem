package com.zeydie.dlpsystem.api.rest.v1;

import com.zeydie.dlpsystem.api.logger.AppLogger;
import com.zeydie.dlpsystem.api.v1.computer.endpoint.IUserEndpoint;
import com.zeydie.dlpsystem.api.v1.computer.reponse.ComputerResponse;
import com.zeydie.dlpsystem.api.v1.computer.request.UserSessionRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final AppLogger logger;

    private final IUserEndpoint userEndpoint;

    private final @NonNull ResponseEntity<ComputerResponse> notFound = ResponseEntity.notFound().build();
    private final @NonNull ResponseEntity<ComputerResponse> badRequest = ResponseEntity.badRequest().build();

    @Async
    @PostMapping("/session")
    public @NotNull CompletableFuture<ResponseEntity<?>> sessionUser(
            @NonNull final HttpServletRequest httpServletRequest,
            @RequestBody @NotNull final UserSessionRequest request
    ) {
        return CompletableFuture.supplyAsync(
                () -> {
                    this.logger.info(
                            "/session -> {} => {}",
                            httpServletRequest.getRemoteAddr(),
                            request
                    );

                    try {
                        return ResponseEntity.ok(this.userEndpoint.sessionUser(httpServletRequest, request));
                    } catch (final Exception exception) {
                        return this.badRequest;
                    }
                }
        );
    }
}
