package com.kpi.routetracker.distance.api.service;

import com.kpi.routetracker.distance.api.responses.DistanceInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
@Slf4j
public class DistanceMatrixServiceStub implements DistanceMatrixService {

    @Override
    public DistanceInfoResponse getDistanceBetween(String origin, String destination) {
        return null;
    }
}
