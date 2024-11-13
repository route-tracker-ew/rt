package com.kpi.routetracker.utils.mapper;

import com.kpi.routetracker.dto.AccountDto;
import com.kpi.routetracker.model.user.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    AccountDto toDto(Account account);
}
