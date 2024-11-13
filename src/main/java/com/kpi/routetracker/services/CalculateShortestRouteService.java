package com.kpi.routetracker.services;

import com.kpi.routetracker.controller.payload.CalculateShortestRoutePayload;
import com.kpi.routetracker.distance.api.service.DistanceMatrixService;
import com.kpi.routetracker.distance.calculator.elements.Edge;
import com.kpi.routetracker.distance.calculator.elements.Vertex;
import com.kpi.routetracker.distance.calculator.impl.TravelingSalesman;
import com.kpi.routetracker.model.parcel.Parcel;
import com.kpi.routetracker.model.route.Route;
import com.kpi.routetracker.services.parcel.ParcelService;
import com.kpi.routetracker.services.route.RouteService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculateShortestRouteService {

    DistanceMatrixService distanceMatrixServiceImpl;

    RouteService routeService;

    ParcelService parcelService;

    public List<String> calculateShortestRoute(CalculateShortestRoutePayload payload) {
        Route route = routeService.getById(payload.routeId());
        //        Calendar calendar = Calendar.getInstance();
        //        calendar.setTime(payload.dateOfDeparture());
        //        int day = calendar.get(Calendar.DAY_OF_WEEK);
        //        if (day != route.getDayOfDepartureFromSource()) {
        //            throw new RuntimeException();
        //        }
        List<Parcel> parcels = parcelService.getAll();
        List<String> vertexList = createVertexes(parcels, route.getSourceCity(), route.getDestinationCity());
        double[][] d = getDistanceBetweenVertexes(vertexList);
        System.out.println(vertexList);
        TravelingSalesman td = new TravelingSalesman(d);
        td.solve();
        //        var s = getOptimalRoute(vertexList);
        return null;
    }

    private List<String> createVertexes(List<Parcel> parcels, String startPoint, String endPoint) {
        List<String> vertexList = new ArrayList<>();
        vertexList.add(startPoint);
        for (Parcel parcel : parcels) {
            if (parcel.getSourceCity() != null && !parcel.getSourceCity().equals(startPoint) && !vertexList.contains(new Vertex(parcel.getSourceCity()))) {
                vertexList.add(parcel.getSourceCity());
            }
            if (!vertexList.contains(parcel.getDestinationCity())) {
                vertexList.add(parcel.getDestinationCity());
            }
        }
        vertexList.add(endPoint);
        return vertexList;

    }

    private double[][] getDistanceBetweenVertexes(List<String> vertexes) {
        double[][] distanceMatrix = new double[vertexes.size()][vertexes.size()];
        for (int i = 0; i < vertexes.size(); i++) {
            for (int k = 0; k < vertexes.size(); k++) {
                if (k != i) {
                    String s = distanceMatrixServiceImpl.getDistanceBetween(vertexes.get(i), vertexes.get(k)).getRows().get(0).getElements()
                            .get(0).getDistance().getText();
                    double d = Double.parseDouble(s.substring(0, s.length() - 3));
                    distanceMatrix[i][k] = d;
                    //                    vertexes.get(i).addEdge(
                    //                            new Edge(vertexes.get(k),
                    //                                    distanceMatrixServiceImpl.getDistanceBetween(vertexes.get(i).getName(), vertexes.get(k).getName()).getRows().get(0).getElements()
                    //                                            .get(0).getDistance().getValue()));
                }

            }

        }
        return distanceMatrix;
    }

    //    private List<Vertex> getOptimalRoute(List<Vertex> vertexList) {
    //        return tsp.runAntColonyOptimization(vertexList, 100, 20, 1, 2, 0.1, 1);
    //    }

}
