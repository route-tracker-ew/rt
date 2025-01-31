package com.kpi.routetracker.repo;

import com.kpi.routetracker.model.route.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByNumber(String number);

    void removeByNumber(String number);
}
