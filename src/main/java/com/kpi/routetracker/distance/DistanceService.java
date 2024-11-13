package com.kpi.routetracker.distance;

import com.kpi.routetracker.distance.api.service.DistanceMatrixService;
import com.kpi.routetracker.model.parcel.Parcel;
import com.kpi.routetracker.model.route.Route;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DistanceService {

    DistanceMatrixService distanceMatrixService;

    public Double[][] getDistanceMatrix(Route route, List<Parcel> parcels) {
        var size = parcels.size() + 2;
        double[][] distances = new double[size][size];

        for (Parcel parcel : parcels) {
            distanceMatrixService.getDistanceBetween(route.getSourceCity(), parcel.getDestinationCity());
        }
        for (int i = 1; i < parcels.size(); i++) {

        }

        return null;
    }
}
