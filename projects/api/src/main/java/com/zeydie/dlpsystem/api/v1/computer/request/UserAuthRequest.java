package com.zeydie.dlpsystem.api.v1.computer.request;

import org.jetbrains.annotations.NotNull;

public record UserAuthRequest(
        @NotNull String login,
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