package com.kpi.routetracker.model.parcel;

import com.kpi.routetracker.model.user.Account;
import com.kpi.routetracker.model.route.Route;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parcel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account sender;

    private String sourceCountry;

    private String sourceCity;

    private String sourceStreet;

    private String sourceHouseNumber;

    private Integer sourceFlatNumber;

    @ManyToOne
    private Account receiver;

    private String destinationCountry;

    private String destinationCity;

    private String destinationStreet;

    private String destinationHouseNumber;

    private Integer destinationFlatNumber;

    private Date estimatedPicKUpDate;

    private Integer amount;

    @Enumerated
    private DeliveryService deliveryService;

    private Integer destinationPostNumber;

    private Double price;

    @Enumerated
    private ParcelStatus parcelStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Route route;

    private boolean request;

    private boolean accept;
}

