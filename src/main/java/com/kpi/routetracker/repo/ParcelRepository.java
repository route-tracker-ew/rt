package com.kpi.routetracker.repo;

import com.kpi.routetracker.model.parcel.Parcel;
import com.kpi.routetracker.model.route.Route;
import com.kpi.routetracker.model.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    List<Parcel> findByRoute(Route route);

    List<Parcel> findAllByEstimatedPicKUpDateAndRoute(Date date, Route route);

    List<Parcel> findAllBySender_PhoneNumber(String senderPhoneNumber);

    List<Parcel> findAllByReceiver_PhoneNumber(String senderPhoneNumber);

    @Query(nativeQuery = true, value = "SELECT * FROM parcel " +
            "WHERE route_id = :routeId AND estimated_pickup_date >= :estimatedPickUpStart AND estimated_pickup_date <= :estimatedPickUpEnd AND request = false AND accept=true")
    List<Parcel> findAllByRouteIdAndEstimatedPickUpDateBetween(Long routeId, Date estimatedPickUpStart, Date estimatedPickUpEnd);

    @Query(nativeQuery = true, value = "SELECT * FROM parcel " +
            "WHERE route_id = :routeId  AND request = false AND accept=true")
    List<Parcel> findAllByRouteIdAndEstimatedPickUpDateBetween2(Long routeId);

    @Query(nativeQuery = true, value = "SELECT * FROM parcel " +
            "WHERE route_id = :routeId And request = true AND accept = false")
    List<Parcel> findAllRequestedByRouteId(Long routeId);

    List<Parcel> getAllByReceiver(Account receiver);
}
