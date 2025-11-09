package com.zeydie.dlpsystem.api.v1.computer.endpoint;

import com.zeydie.dlpsystem.api.v1.computer.request.UserAuthRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;

public interface IUserEndpoint {
    boolean authUser(
            @NonNull final HttpServletRequest httpServletRequest,
            @NonNull final UserAuthRequest request
    ) throws Exception;
}
