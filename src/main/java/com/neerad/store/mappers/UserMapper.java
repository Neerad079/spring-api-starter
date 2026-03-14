package com.neerad.store.mappers;

import com.neerad.store.dtos.RegisterUserRequest;
import com.neerad.store.dtos.UpdateUserRequest;
import com.neerad.store.dtos.UserDto;
import com.neerad.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Maps object automatically during compile time , no need to map object manually
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void updateEntity(UpdateUserRequest request, @MappingTarget User user);
}
