package com.zeydie.dlpsystem.api.services;

import com.zeydie.dlpsystem.api.v1.computer.request.UserAuthRequest;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public interface IUserService {
    boolean authUser(
            @NonNull String login,
            @NonNull UserAuthRequest.AuthType type
    );
}