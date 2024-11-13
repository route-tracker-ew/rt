package com.kpi.routetracker.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record CalculateShortestRoutePayload(
        @NotNull(message = "route id field should not be null")
        @Min(value = 1, message = "route id field should be grater than 0 ")
        Long routeId,
        @NotNull(message = "date of departure field should not be null")
        Date dateOfDeparture) {

}
