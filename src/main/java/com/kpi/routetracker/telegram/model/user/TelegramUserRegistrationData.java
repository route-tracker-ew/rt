package com.kpi.routetracker.telegram.model.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramUserRegistrationData {

    String phoneNumber;
    String firstName;
    String lastName;
    Date lastUpdatedTime;
}
