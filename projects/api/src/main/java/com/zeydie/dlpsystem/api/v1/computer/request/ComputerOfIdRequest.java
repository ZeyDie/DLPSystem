package com.zeydie.dlpsystem.api.v1.computer.request;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record ComputerOfIdRequest(@Nullable UUID computerId){
}
