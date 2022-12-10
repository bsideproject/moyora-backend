package com.beside.ties.domain.account.mapper;

import com.beside.ties.domain.account.Account;
import com.beside.ties.dto.account.response.LoginResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mappings({
            @Mapping(source = "id",target = "userId"),
            @Mapping(target = "isFirst", ignore = true),
            @Mapping(target = "profileImageUrl", source = "profile")
    })
    LoginResponseDto toLoginResponseDto(Account account);

}
