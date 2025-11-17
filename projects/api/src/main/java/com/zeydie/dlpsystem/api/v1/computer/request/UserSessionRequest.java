package com.zeydie.dlpsystem.api.v1.computer.request;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record UserSessionRequest(
        @NotNull String login,
        @Nullable String domain,
        @NotNull AuthType authType
) {
    public enum AuthType {
        Logon,
        Logoff,
        Lock,
        Unlock,
        RemoteConnect,
        RemoteDisconnect,
        ConsoleConnect,
        ConsoleDisconnect,
        RemoteControl,
        Unnamed;
    }
}