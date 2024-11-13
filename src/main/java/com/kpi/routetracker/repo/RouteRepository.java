package com.kpi.routetracker.repo;

import com.kpi.routetracker.model.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query(nativeQuery = true, value = "SELECT r.* " +
            "FROM route r " +
            "JOIN route_owners ro ON r.id = ro.route_id " +
            "JOIN account a ON ro.account_id = a.id " +
            "WHERE a.phone_number = :phoneNumber")
   List<Route> findByOwnerNumber(@Param("phoneNumber") String phoneNumber);

    @Query(nativeQuery = true, value = "SELECT r.* " +
            "FROM route r " +
            "JOIN route_workers rw ON r.id = rw.route_id " +
            "JOIN account a ON rw.account_id = a.id " +
            "WHERE a.phone_number = :phoneNumber")
    List<Route> findByWorkerNumber(@Param("phoneNumber") String phoneNumber);
}
