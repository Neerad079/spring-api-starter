package com.neerad.store.mappers;

import com.neerad.store.dtos.UserDto;
import com.neerad.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target= "createdAt" ,expression = "java(java.time.LocalDateTime.now())")
    // Maps object automatically during compile time , no need to map object manually
    UserDto toDto(User user);
}
