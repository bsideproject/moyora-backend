package com.beside.ties.domain.users.mapper;

import com.beside.ties.domain.users.Users;
import com.beside.ties.dto.user.response.LoginResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    @Mappings({
            @Mapping(source = "id",target = "userId"),
            @Mapping(target = "isFirst", ignore = true),
            @Mapping(target = "profileImageUrl", source = "profile")
    })
    LoginResponseDto toLoginResponseDto(Users users);

}
