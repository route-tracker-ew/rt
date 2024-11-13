package com.kpi.routetracker.distance.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Element {

    String origin;
    String destination;
    String status;
    Distance distance;
    Duration duration;
}
