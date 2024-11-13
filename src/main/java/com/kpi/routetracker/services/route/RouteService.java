package com.kpi.routetracker.services.route;

import com.kpi.routetracker.controller.payload.NewRoutePayload;
import com.kpi.routetracker.model.route.Car;
import com.kpi.routetracker.model.route.Route;
import com.kpi.routetracker.model.user.Account;
import com.kpi.routetracker.repo.RouteRepository;
import com.kpi.routetracker.services.account.AccountService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RouteService {

    RouteRepository repository;

    AccountService accountService;

    CarService carService;

    public List<Route> getAll() {
        return repository.findAll();
    }

    public Route create(NewRoutePayload payload) {
        Optional<Account> account = accountService.getAccountByPhoneNumber(payload.ownerPhoneNumber());
        if (account.isPresent()) {
            return repository.save(Route.builder()
                    .sourceCountry(payload.sourceCountry())
                    .sourceCity(payload.sourceCity())
                    .dayOfDepartureFromSource(payload.dayOfDepartureFromSource())
                    .destinationCountry(payload.destinationCountry())
                    .destinationCity(payload.destinationCity())
                    .dayOfDepartureFromDestination(payload.dayOfDepartureFromDestination())
                    .owners(List.of(account.get()))
                    .workers(List.of(account.get()))
                    .build());
        }
        throw new IllegalArgumentException("User with phoneNumber: " + payload.ownerPhoneNumber() + " not found");
    }

    public Route getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Route with id: " + id + " not found"));
    }

    public List<Route> getByOwnerPhoneNumber(String ownerPhoneNumber) {
        return repository.findByOwnerNumber(ownerPhoneNumber);
    }

    public List<Route> getByWorkerPhoneNumber(String ownerPhoneNumber) {
        return repository.findByWorkerNumber(ownerPhoneNumber);
    }

    public Route shareRoute(Long id, String newOwnerPhoneNumber) {
        Route route = getById(id);
        Account newOwner = accountService.getAccountByPhoneNumber(newOwnerPhoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("User with phoneNumber: " + newOwnerPhoneNumber + " not found"));
        route = route.addNewOwner(newOwner) ? repository.save(route) : route;
        route = route.addNewWorker(newOwner) ? repository.save(route) : route;
        return route;
    }

    public Route hireWorkerOnRoute(Long id, String workerPhoneNumber) {
        Route route = getById(id);
        Account newWorker = accountService.getAccountByPhoneNumber(workerPhoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("User with phoneNumber: " + workerPhoneNumber + " not found"));
        route = route.addNewWorker(newWorker) ? repository.save(route) : route;
        return route;
    }

    public Route leaveWorkerOnRoute(Long id, String workerPhoneNumber) {
        Route route = getById(id);
        Account worker = accountService.getAccountByPhoneNumber(workerPhoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("User with phoneNumber: " + workerPhoneNumber + " not found"));
        route = route.deleteWorker(worker) ? repository.save(route) : route;
        return route;
    }

    public Route hireCarOnRoute(Long id, String carNumber) {
        Route route = getById(id);
        Car car = carService.getByNumber(carNumber).orElseThrow(() -> new IllegalArgumentException("Car with number: " + carNumber + " not found"));
        route = route.addNewCar(car) ? repository.save(route) : route;
        return route;
    }

    public Route liveCarOnRoute(Long id, String carNumber) {
        Route route = getById(id);
        Car car = carService.getByNumber(carNumber).orElseThrow(() -> new IllegalArgumentException("Car with number: " + carNumber + " not found"));
        route = route.leaveNewCar(car) ? repository.save(route) : route;
        carService.deleteCarByCarNumber(carNumber);
        return route;
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
