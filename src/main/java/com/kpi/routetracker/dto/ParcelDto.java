package com.kpi.routetracker.dto;

import com.kpi.routetracker.model.parcel.DeliveryService;
import com.kpi.routetracker.model.parcel.ParcelStatus;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelDto {
    Long id;

    AccountDto sender;

    String sourceCountry;

    String sourceCity;

    String sourceStreet;

    String sourceHouseNumber;

    Integer sourceFlatNumber;

    AccountDto receiver;

    String destinationCountry;

    String destinationCity;

    String destinationStreet;

    String destinationHouseNumber;

    Integer destinationFlatNumber;

    Date estimatedPicKUpDate;

    Integer amount;

    @Enumerated
    DeliveryService deliveryService;

    Integer destinationPostNumber;

    Double price;

    ParcelStatus parcelStatus;

    RouteDto route;
}
