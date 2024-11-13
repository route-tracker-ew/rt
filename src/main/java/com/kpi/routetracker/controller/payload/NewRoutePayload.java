package com.kpi.routetracker.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewRoutePayload(
        @NotNull(message = "Source country field should not be null")
        @NotEmpty(message = "Source country field should not be empty")
        String sourceCountry,

        @NotNull(message = "Source city field should not be null")
        @NotEmpty(message = "Source city field should not be empty")
        String sourceCity,

        @NotNull(message = "Day of departure from source field should not be null")
        @Min(value = 0, message = "Day of departure from source field should be grater than 0 or equal")
        Integer dayOfDepartureFromSource,

        @NotNull(message = "Destination country field should not be null")
        @NotEmpty(message = "Destination country field should not be empty")
        String destinationCountry,

        @NotNull(message = "Destination city field should not be null")
        @NotEmpty(message = "Destination city field should not be empty")
        String destinationCity,

        @NotNull(message = "Day of departure from Destination field should not be null")
        @Min(value = 0, message = "Day of departure from Destination field should be grater than 0 or equal")
        Integer dayOfDepartureFromDestination,
        @NotNull(message = "Owner phone number field should not be null")
        @NotEmpty(message = "Owner phone number field should not be empty")
        String ownerPhoneNumber
) {

}
