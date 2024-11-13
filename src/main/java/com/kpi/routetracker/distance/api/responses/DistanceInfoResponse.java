package com.kpi.routetracker.distance.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceInfoResponse {

    List<String> destinationAddresses;
    List<String> originAddresses;
    List<Row> rows;
    String status;
}





