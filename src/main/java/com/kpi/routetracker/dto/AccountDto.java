package com.kpi.routetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    Long id;

    String firstName;

    String lastName;

    String phoneNumber;

}
