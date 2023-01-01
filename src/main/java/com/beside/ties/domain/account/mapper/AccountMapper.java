package com.beside.ties.domain.account.mapper;

import com.beside.ties.domain.account.dto.request.AccountUpdateRequest;
import com.beside.ties.domain.account.dto.response.AccountInfoResponse;
import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.dto.response.LoginResponse;
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
    LoginResponse toLoginResponseDto(Account account);

    @Mappings({
            @Mapping(target = "schoolName", source = "school.schoolName"),
            @Mapping(target = "state", source = "region.parent.name"),
            @Mapping(target = "city", source = "region.name"),
            @Mapping(target = "job", source = "myJob.name"),
            @Mapping(target = "phone", source = "phoneNum")
    })
    AccountInfoResponse toAccountInfoResponse(Account account);

}
