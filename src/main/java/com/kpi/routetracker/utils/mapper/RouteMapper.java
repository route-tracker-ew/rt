package com.kpi.routetracker.utils.mapper;

import com.kpi.routetracker.dto.RouteDto;
import com.kpi.routetracker.model.route.Route;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    RouteMapper mapper = Mappers.getMapper(RouteMapper.class);

    RouteDto toDto(Route route);

    List<RouteDto> toDto(List<Route> routes);
}
