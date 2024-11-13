package com.kpi.routetracker.controller.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewGpsTrackerPayload(

        @NotNull(message = "Car Number field should not be null")
        @NotEmpty(message = "Car Number field should not be empty")
        String carNumber,
        @NotNull(message = "Phone number field should not be null")
        @NotEmpty(message = "Phone number field should not be empty")
        String phoneNumber,
        @NotNull(message = "Password field should not be null")
        @NotEmpty(message = "Password field should not be empty")
        @Size(min = 5, max = 30, message = "Size of password should be between 5 and 30")
        String password,
        @NotNull(message = "Copy of password field should not be null")
        @NotEmpty(message = "Copy of password field should not be empty")
        @Size(min = 5, max = 30, message = "Size copy of password should be between 5 and 30")
        String cfmPass) {

}
