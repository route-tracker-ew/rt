package com.kpi.routetracker.distance.calculator.elements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Edge {

    Vertex targetVertex;
    long distance;
}
