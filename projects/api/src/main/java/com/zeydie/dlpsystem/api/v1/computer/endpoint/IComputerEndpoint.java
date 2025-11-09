package com.zeydie.dlpsystem.api.v1.computer.endpoint;

import com.zeydie.dlpsystem.api.v1.computer.reponse.ComputerResponse;
import com.zeydie.dlpsystem.api.v1.computer.request.ComputerUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IComputerEndpoint {
    @Nullable ComputerResponse getComputerById(
            @NonNull final HttpServletRequest servletRequest,
            @NonNull final UUID computerId
    ) throws Exception;

    @Nullable ComputerResponse getComputerByName(
            @NonNull final HttpServletRequest servletRequest,
            @NonNull final String name
    ) throws Exception;

    @Nullable ComputerResponse updateComputer(
            @NonNull final HttpServletRequest httpServletRequest,
            @NonNull final ComputerUpdateRequest request
    ) throws Exception;
}