package com.zeydie.dlpsystem.server.endpoints;

import com.zeydie.dlpsystem.api.services.IComputerService;
import com.zeydie.dlpsystem.api.v1.computer.endpoint.IComputerEndpoint;
import com.zeydie.dlpsystem.api.v1.computer.reponse.ComputerResponse;
import com.zeydie.dlpsystem.api.v1.computer.request.ComputerUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ComputerEndpoint implements IComputerEndpoint {
    private final IComputerService computerService;

    @Override
    public @Nullable ComputerResponse getComputerById(
            @NonNull final HttpServletRequest servletRequest,
            @NonNull final UUID computerId
    ) {
        @Nullable val computerDTO = this.computerService.getComputerById(computerId);

        if (computerDTO == null) return null;

        return ComputerResponse.builder()
                .name(computerDTO.name())
                .active(computerDTO.active())
                .lastStartup(computerDTO.lastStartup())
                .lastUpdate(computerDTO.lastUpdate())
                .build();
    }

    @Override
    public @Nullable ComputerResponse getComputerByName(
            @NonNull final HttpServletRequest servletRequest,
            @NonNull final String name
    ) {
        @Nullable val computerDTO = this.computerService.getComputerByName(name);

        if (computerDTO == null) return null;

        return ComputerResponse.builder()
                .name(computerDTO.name())
                .active(computerDTO.active())
                .lastStartup(computerDTO.lastStartup())
                .lastUpdate(computerDTO.lastUpdate())
                .build();
    }

    @Override
    public @Nullable ComputerResponse updateComputer(
            @NonNull final HttpServletRequest httpServletRequest,
            @NonNull final ComputerUpdateRequest request
    ) {
        @Nullable val name = request.name();
        @Nullable val ip = request.ip();
        @Nullable val domain  = request.domain();
        val lastStartup = request.lastStartup();

        if (name == null || ip == null)
            return null;

        @Nullable val computerDTO = this.computerService.updateComputer(
                name,
                ip,
                domain,
                lastStartup
        );

        if (computerDTO == null) return null;

        return ComputerResponse.builder()
                .name(computerDTO.name())
                .active(computerDTO.active())
                .lastStartup(computerDTO.lastStartup())
                .lastUpdate(computerDTO.lastUpdate())
                .build();
    }
}