package com.zeydie.dlpsystem.api.services;

import com.zeydie.dlpsystem.api.v1.computer.request.UserSessionRequest;
import lombok.NonNull;

public interface IUserService {
    boolean sessionUser(
            @NonNull String login,
            @NonNull UserSessionRequest.AuthType type
    );
}