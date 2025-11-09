package com.zeydie.dlpsystem.api.services;

import lombok.NonNull;

public interface ILdapService {
    boolean isComputerExist(@NonNull final String name);
}
