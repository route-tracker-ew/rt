package com.kpi.routetracker.services.route;

import com.kpi.routetracker.controller.payload.NewCarPayload;
import com.kpi.routetracker.model.route.Car;
import com.kpi.routetracker.model.user.Account;
import com.kpi.routetracker.repo.CarRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarService {

    CarRepository repository;

    public Optional<Car> getByNumber(String number) {
        return repository.findByNumber(number);
    }

    public Car create(NewCarPayload payload) {
        if (getByNumber(payload.number()).isEmpty()) {
            return repository.save(Car.builder()
                    .brand(payload.brand())
                    .model(payload.model())
                    .color(payload.color())
                    .number(payload.number())
                    .engineCapacity(payload.engineCapacity())
                    .build());

        }
        throw new IllegalArgumentException("Car with number: " + payload.number() + "is already present");
    }

    public boolean deleteCarByCarNumber(String number) {
        repository.removeByNumber(number);
        return getByNumber(number).isEmpty();
    }

    @Transactional
    public Car addGpsTracker(String carNumber, Account account) {
        Car car = getByNumber(carNumber)
                .orElseThrow(() -> new NotFoundException("Car with number " + carNumber + " not found"));
        car.setGpsTracker(account);
        return repository.save(car);
    }
}
