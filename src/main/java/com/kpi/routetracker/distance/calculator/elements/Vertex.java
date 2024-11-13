package com.kpi.routetracker.distance.calculator.elements;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Vertex {

    String name;
    List<Edge> edges = new ArrayList<>();

    public Vertex(String name) {
        this.name = name;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }
}
