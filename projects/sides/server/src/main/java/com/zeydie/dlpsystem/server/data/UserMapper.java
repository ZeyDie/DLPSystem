package com.zeydie.dlpsystem.server.data;

import com.zeydie.dlpsystem.server.data.dto.UserDTO;
import com.zeydie.dlpsystem.server.data.entity.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    @NotNull UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Nullable UserDTO toDTO(@Nullable final UserEntity userEntity);

    @Nullable UserEntity toEntity(@Nullable final UserDTO userDTO);
}