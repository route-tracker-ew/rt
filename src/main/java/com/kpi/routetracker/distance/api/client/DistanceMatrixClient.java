package com.kpi.routetracker.distance.api.client;

import com.kpi.routetracker.distance.api.responses.DistanceInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "DistanceMatrixAPI", url = "${distance-matrix.path}")
public interface DistanceMatrixClient {

    @GetMapping
    ResponseEntity<DistanceInfoResponse> getDistanceInfoResponse(@RequestParam String origins, @RequestParam String destinations, @RequestParam String key);
}
