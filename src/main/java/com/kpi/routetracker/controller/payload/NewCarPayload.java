package com.kpi.routetracker.controller.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewCarPayload(
        @NotNull(message = "Brand field should not be null")
        @NotEmpty(message = "Brand field should not be empty")
        String brand,

        @NotNull(message = "Model field should not be null")
        @NotEmpty(message = "Model field should not be empty")
        String model,

        String color,

        @NotNull(message = "Number field should not be null")
        @NotEmpty(message = "Number field should not be empty")
        String number,

        Double engineCapacity
) {

}
