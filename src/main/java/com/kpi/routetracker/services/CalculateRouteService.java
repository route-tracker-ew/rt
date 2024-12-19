package com.kpi.routetracker.services;

import com.kpi.routetracker.controller.AntOptimizationService;
import com.kpi.routetracker.controller.payload.CalculateRoutePayload;
import com.kpi.routetracker.distance.api.service.DistanceMatrixService;
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
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculateRouteService {

    DistanceMatrixService distanceMatrixServiceImpl;

    RouteService routeService;

    ParcelService parcelService;

    private double parseDistance(String distanceText) {
        // Парсимо строку, наприклад, "120 km" -> 120.0
        return Double.parseDouble(distanceText.replace(" km", "").replace(",", ".").trim());
    }

    public Route getResponsibleRoute(Long routeId) {
        return routeService.getById(routeId);
    }

    public List<Parcel> getResponsibleParcels(Route route, Date departureData) {
        return parcelService.getAll();
    }

    public List<String> calculateRoute(CalculateRoutePayload payload) {
        Route route = getResponsibleRoute(payload.routeId());
        List<String> parcelCity = new ArrayList<>();
        parcelCity.add(route.getSourceCity());
        List<Parcel> parcels = getResponsibleParcels(route, payload.dateOfDeparture());
        for (Parcel parcel : parcels) {
            if (parcel.getSourceCity() != null && !parcelCity.contains(parcel.getSourceCity())) {
                parcelCity.add(parcel.getSourceCity());
            }
            if (parcel.getDestinationCity() != null && !parcelCity.contains(parcel.getDestinationCity())) {
                parcelCity.add(parcel.getDestinationCity());
            }
        }
        double[][] distanceMatrix = createDistanceMatrix(parcelCity);
        AntOptimizationService antOptimizationService = new AntOptimizationService(distanceMatrix);
        var antResult = antOptimizationService.solve(0);
        List<String> result = new ArrayList<>();
        for (int i : antResult) {
            result.add(parcelCity.get(i));
        }
        return result;

    }

    public double[][] createDistanceMatrix(List<String> cities) {
        int cityCount = cities.size();
        double[][] distanceMatrix = new double[cityCount][cityCount];

        for (int i = 0; i < cityCount; i++) {
            for (int j = 0; j < cityCount; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0;  // Відстань від міста до самого себе дорівнює 0
                } else {
                    String cityFrom = cities.get(i);
                    String cityTo = cities.get(j);

                    // Використовуємо ваш сервіс для отримання відстані між двома містами
                    String distanceText = distanceMatrixServiceImpl.getDistanceBetween(cityFrom, cityTo)
                            .getRows().get(0).getElements().get(0).getDistance().getText();

                    // Парсимо відстань (якщо відстань в кілометрах, можна перетворити на double)
                    double distance = parseDistance(distanceText);
                    distanceMatrix[i][j] = distance;
                }
            }
        }

        return distanceMatrix;
    }
}
