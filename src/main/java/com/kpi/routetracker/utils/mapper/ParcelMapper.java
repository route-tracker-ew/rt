package com.kpi.routetracker.utils.mapper;

import com.kpi.routetracker.dto.ParcelDto;
import com.kpi.routetracker.model.parcel.Parcel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParcelMapper {

    ParcelMapper mapper = Mappers.getMapper(ParcelMapper.class);

    ParcelDto toDto(Parcel parcel);

    List<ParcelDto> toDto(List<Parcel> parcels);
}
