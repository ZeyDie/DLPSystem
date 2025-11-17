package com.zeydie.dlpsystem.api.v1.computer.endpoint;

import com.zeydie.dlpsystem.api.v1.computer.request.UserSessionRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;

public interface IUserEndpoint {
    boolean sessionUser(
            @NonNull final HttpServletRequest httpServletRequest,
            @NonNull final UserSessionRequest request
    ) throws Exception;
}
