package com.kpi.routetracker.services.account;

import com.kpi.routetracker.controller.payload.NewAccountPayload;
import com.kpi.routetracker.controller.payload.NewGpsTrackerPayload;
import com.kpi.routetracker.model.user.Account;
import com.kpi.routetracker.repo.AccountRepository;
import com.kpi.routetracker.services.route.CarService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountService {

    final AccountRepository repository;
    final PasswordEncoder passwordEncoder;

    final CarService carService;

    public Optional<Account> getAccountByPhoneNumber(String phoneNumber) {
         return repository.getByPhoneNumber(phoneNumber);
    }

    public Account create(NewAccountPayload payload) {
        if (getAccountByPhoneNumber(payload.phoneNumber()).isEmpty()) {
            return repository.save(Account.builder()
                    .firstName(payload.firstName())
                    .lastName(payload.lastName())
                    .phoneNumber(payload.phoneNumber())
                    .password(passwordEncoder.encode(payload.password()))
                    .isRegistered(true)
                    .build());
        }
        throw new IllegalArgumentException("User with phoneNumber: " + payload.phoneNumber() + " found");
    }

    public Account createGps(NewGpsTrackerPayload payload) {
        if (carService.getByNumber(payload.carNumber()).isPresent() && getAccountByPhoneNumber(payload.phoneNumber()).isEmpty()) {
            Account account = repository.save(Account.builder()
                    .phoneNumber(payload.phoneNumber())
                    .password(passwordEncoder.encode(payload.password()))
                    .isRegistered(true)
                    .isGpsTracker(true)
                    .build());

            carService.addGpsTracker(payload.carNumber(), account);
            return account;
        }
        throw new IllegalArgumentException("User with phoneNumber: " + payload.phoneNumber() + " found");
    }

    public Account createWithoutPassword(String phoneNumber, String firstName, String lastName) {
        return repository.save(Account.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .isRegistered(false)
                .build());
    }

}


