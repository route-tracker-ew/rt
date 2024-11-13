package com.kpi.routetracker.distance.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Distance {
    String text;
    Integer value;
}
