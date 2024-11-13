package com.kpi.routetracker.model.route;

import com.kpi.routetracker.model.user.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String sourceCountry;

    String sourceCity;

    Integer dayOfDepartureFromSource;

    String destinationCountry;

    String destinationCity;

    Integer dayOfDepartureFromDestination;

    @ManyToMany
    @JoinTable(
            name = "route_owners",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    List<Account> owners;

    @ManyToMany
    @JoinTable(
            name = "route_workers",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    List<Account> workers;

    @ManyToMany
    @JoinTable(
            name = "route_cars",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "car_id")
    )
    List<Car> cars;

    public boolean addNewOwner(Account account) {
        if (this.owners == null || this.owners.isEmpty()) {
            this.owners = new ArrayList<>();
        }
        if (!owners.contains(account)) {
            this.owners.add(account);
            return true;
        }
        return false;
    }

    public boolean addNewWorker(Account account) {
        if (this.workers == null || this.workers.isEmpty()) {
            this.workers = new ArrayList<>();
        }
        if (!workers.contains(account)) {
            this.workers.add(account);
            return true;
        }
        return false;
    }

    public boolean deleteWorker(Account account) {
        if (this.workers == null || this.workers.isEmpty()) {
            this.workers = new ArrayList<>();
        }
        if (workers.contains(account)) {
            this.workers.remove(account);
            return true;
        }
        return false;
    }

    public boolean addNewCar(Car car) {
        if (this.cars == null || this.cars.isEmpty()) {
            this.cars = new ArrayList<>();
        }
        if (!cars.contains(car)) {
            this.cars.add(car);
            return true;
        }
        return false;
    }

    public boolean leaveNewCar(Car car) {
        if (this.cars == null || this.cars.isEmpty()) {
            this.cars = new ArrayList<>();
        }
        if (cars.contains(car)) {
            this.cars.remove(car);
            return true;
        }
        return false;
    }
}