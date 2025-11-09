package com.zeydie.dlpsystem.server.data;

import com.zeydie.dlpsystem.server.data.dto.ComputerDTO;
import com.zeydie.dlpsystem.server.data.entity.ComputerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ComputerMapper {
    @NotNull ComputerMapper INSTANCE = Mappers.getMapper(ComputerMapper.class);

    @Nullable ComputerDTO toDTO(@Nullable final ComputerEntity computerEntity);

    @Nullable ComputerEntity toEntity(@Nullable final ComputerDTO computerDTO);
}
