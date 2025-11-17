package com.zeydie.dlpsystem.server.endpoints;

import com.zeydie.dlpsystem.api.services.IUserService;
import com.zeydie.dlpsystem.api.v1.computer.endpoint.IUserEndpoint;
import com.zeydie.dlpsystem.api.v1.computer.request.UserSessionRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEndpoint implements IUserEndpoint {
    private final IUserService userService;

    @Override
    public boolean sessionUser(@NonNull final HttpServletRequest httpServletRequest, @NonNull final UserSessionRequest request) throws Exception {
        return this.userService.sessionUser(request.login(), request.authType());
    }
}