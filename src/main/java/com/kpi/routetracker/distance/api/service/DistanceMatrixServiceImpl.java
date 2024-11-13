package com.kpi.routetracker.distance.api.service;

import com.kpi.routetracker.distance.api.client.DistanceMatrixClient;
import com.kpi.routetracker.distance.api.responses.DistanceInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
@Slf4j
public class DistanceMatrixServiceImpl implements DistanceMatrixService {

    //
    private final DistanceMatrixClient distanceMatrixClient;

    @Value("${distance-matrix.key}")
    private String key;

    public DistanceMatrixServiceImpl(DistanceMatrixClient distanceMatrixClient) {
        this.distanceMatrixClient = distanceMatrixClient;
    }

    @Override
    public DistanceInfoResponse getDistanceBetween(String origin, String destination) {
        return distanceMatrixClient.getDistanceInfoResponse(origin, destination, key).getBody();
    }

}
