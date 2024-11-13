package com.kpi.routetracker.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record GetSpecifiedParcelsPayload(
        @NotNull(message = "Route id field should not be null")
        @NotEmpty(message = "Route id field should not be empty")
        @Min(value = 1, message = "RouteId field should be grater than 0")
        Long routeId,

        @NotNull(message = "Estimated pick-up date should not be null")
        Date estimatedPickUp
) {

}
