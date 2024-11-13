package com.kpi.routetracker.distance.api.service;

import com.kpi.routetracker.distance.api.responses.DistanceInfoResponse;

public interface DistanceMatrixService {

    DistanceInfoResponse getDistanceBetween(String origin, String destination);
}
