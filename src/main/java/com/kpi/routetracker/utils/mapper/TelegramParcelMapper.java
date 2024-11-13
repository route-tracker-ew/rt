package com.kpi.routetracker.utils.mapper;

import com.kpi.routetracker.dto.TelegramParcelDto;
import com.kpi.routetracker.model.parcel.Parcel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TelegramParcelMapper {

    TelegramParcelMapper mapper = Mappers.getMapper(TelegramParcelMapper.class);

    TelegramParcelDto toDto(Parcel parcel);

    List<TelegramParcelDto> toDto(List<Parcel> parcels);
}
