package com.neerad.store.mappers;

import com.neerad.store.dtos.UserDto;
import com.neerad.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Maps object automatically during compile time , no need to map object manually
    UserDto toDto(User user);
}
