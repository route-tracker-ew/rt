package com.kpi.routetracker.dto;

import com.kpi.routetracker.model.route.Car;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {

    Long id;

    String sourceCountry;

    String sourceCity;

    Integer dayOfDepartureFromSource;

    String destinationCountry;

    String destinationCity;

    Integer dayOfDepartureFromDestination;

    List<AccountDto> owners;

    List<AccountDto> workers;

    List<Car> cars;
}
