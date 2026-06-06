package com.trimq.user.mapper;

import com.trimq.user.dto.AuthResponse;
import com.trimq.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for User entity.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", expression = "java(user.getId().toString())")
    AuthResponse.UserDto toUserDto(User user);
}

