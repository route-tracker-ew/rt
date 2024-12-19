package com.kpi.routetracker.distance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialClass {

    private String from;
    private String to;
    private double distance;

}
