package com.zeydie.dlpsystem.api.v1.computer.request;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public record ComputerOfNameRequest(@Nullable String name) {
}