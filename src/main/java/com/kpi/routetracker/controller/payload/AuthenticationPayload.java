package com.kpi.routetracker.controller.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthenticationPayload(
        @NotNull(message = "Login field should not be null")
        @NotEmpty(message = "Login field should not be empty")
        String login,

        @NotNull(message = "Copy of password field should not be null")
        @NotEmpty(message = "Copy of password field should not be empty")
        @Size(min = 5, max = 30, message = "Size copy of password should be between 5 and 30")
        String password
) {

}
