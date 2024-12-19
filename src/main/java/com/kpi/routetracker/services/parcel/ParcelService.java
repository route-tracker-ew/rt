package com.kpi.routetracker.services.parcel;

import com.kpi.routetracker.controller.payload.NewParcelPayload;
import com.kpi.routetracker.controller.payload.UpdateParcelPayload;
import com.kpi.routetracker.model.parcel.Parcel;
import com.kpi.routetracker.model.parcel.ParcelStatus;
import com.kpi.routetracker.model.route.Route;
import com.kpi.routetracker.model.user.Account;
import com.kpi.routetracker.notification.TelegramNotificationService;
import com.kpi.routetracker.repo.ParcelRepository;
import com.kpi.routetracker.services.account.AccountService;
import com.kpi.routetracker.services.route.RouteService;
import jakarta.ws.rs.NotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ParcelService {

    ParcelRepository repository;

    RouteService routeService;

    AccountService accountService;

    TelegramNotificationService telegramNotificationService;

    public List<Parcel> getAll() {
        return repository.findAll();
    }

    public List<Parcel> getRequestParcels(String phoneNumber) {
        List<Route> routes = routeService.getByOwnerPhoneNumber(phoneNumber);
        List<Parcel> parcels = new ArrayList<>();
        for (Route route : routes) {
            if (route != null) {
                parcels.addAll(repository.findAllRequestedByRouteId(route.getId()));
            }
        }
        return parcels;
    }

    public List<Parcel> getSpecifiedParcels(Long routeId, Date estimatedPickUp, String workerPhone) {
        var route = routeService.getById(routeId);
        var account = accountService.getAccountByPhoneNumber(workerPhone).orElseThrow(NotFoundException::new);

        var dates = calculateDepartureDates(estimatedPickUp, route.getDayOfDepartureFromSource(), route.getDayOfDepartureFromDestination());

        if (route.getWorkers().contains(account)) {
            var r = repository.findAllByRouteIdAndEstimatedPickUpDateBetween(route.getId(), dates.getLeft(), dates.getRight());
            return repository.findAllByRouteIdAndEstimatedPickUpDateBetween2(route.getId());
        }
        return null;
    }

    protected Pair<Date, Date> calculateDepartureDates(Date estimatedPickUpDate, int dayOfDepartureFromSource, int dayOfDepartureFromDestination) {
        Calendar departureFromSource = Calendar.getInstance();
        departureFromSource.setTime(estimatedPickUpDate);
        departureFromSource.set(Calendar.DAY_OF_WEEK, dayOfDepartureFromSource);

        Calendar departureFromDestination = (Calendar) departureFromSource.clone();
        departureFromDestination.set(Calendar.DAY_OF_WEEK, dayOfDepartureFromDestination);

        return Pair.of(departureFromSource.getTime(), departureFromDestination.getTime());
    }

    public Parcel create(NewParcelPayload payload) {
        Route route = routeService.getById(payload.routeId());
        Account sender = getAccount(payload.senderPhoneNumber(), payload.senderFirstName(), payload.senderLastName());
        Account receiver = getAccount(payload.receiverPhoneNumber(), payload.receiverFirstName(), payload.receiverLastName());
        var parcel = repository.save(Parcel.builder()
                .sender(sender)
                .sourceCountry(payload.sourceCountry())
                .sourceCity(payload.sourceCity())
                .sourceStreet(payload.sourceStreet())
                .sourceHouseNumber(payload.sourceHouseNumber())
                .sourceFlatNumber(payload.sourceFlatNumber())
                .receiver(receiver)
                .destinationCountry(payload.destinationCountry())
                .destinationCity(payload.destinationCity())
                .destinationStreet(payload.destinationStreet())
                .destinationHouseNumber(payload.destinationHouseNumber())
                .destinationFlatNumber(payload.destinationFlatNumber())
                .estimatedPicKUpDate(payload.estimatedPicKUpDate())
                .amount(payload.amount())
                //                .deliveryService(payload.deliveryService())
                .destinationPostNumber(payload.destinationPostNumber())
                .price(payload.price())
                .parcelStatus(ParcelStatus.CHECKING)
                .route(route)
                .accept(true)
                .build());

        telegramNotificationService.sendTelegramParcelNotification(parcel);
        return parcel;
    }

    public Parcel update(UpdateParcelPayload payload) {
        var savedParcel = getById(payload.id());

        if (savedParcel != null) {
            Account sender = getAccount(payload.senderPhoneNumber(), payload.senderFirstName(), payload.senderLastName());
            Account receiver = getAccount(payload.receiverPhoneNumber(), payload.receiverFirstName(), payload.receiverLastName());

            savedParcel.setSender(sender);
            savedParcel.setSourceCountry(payload.sourceCountry());
            savedParcel.setSourceCity(payload.sourceCity());
            savedParcel.setSourceStreet(payload.sourceStreet());
            savedParcel.setSourceHouseNumber(payload.sourceHouseNumber());
            savedParcel.setSourceFlatNumber(payload.sourceFlatNumber());
            savedParcel.setReceiver(receiver);
            savedParcel.setDestinationCountry(payload.destinationCountry());
            savedParcel.setDestinationCity(payload.destinationCity());
            savedParcel.setDestinationStreet(payload.destinationStreet());
            savedParcel.setDestinationHouseNumber(payload.destinationHouseNumber());
            savedParcel.setDestinationFlatNumber(payload.destinationFlatNumber());
            savedParcel.setAmount(payload.amount());
            savedParcel.setPrice(payload.price());

            var newParcel = repository.save(savedParcel);
            telegramNotificationService.sendTelegramParcelNotification(newParcel);
            return newParcel;
        }
        return null;
    }

    public Parcel accept(UpdateParcelPayload payload) {
        var savedParcel = getById(payload.id());

        if (savedParcel != null) {
            Account sender = getAccount(payload.senderPhoneNumber(), payload.senderFirstName(), payload.senderLastName());
            Account receiver = getAccount(payload.receiverPhoneNumber(), payload.receiverFirstName(), payload.receiverLastName());

            savedParcel.setSender(sender);
            savedParcel.setSourceCountry(payload.sourceCountry());
            savedParcel.setSourceCity(payload.sourceCity());
            savedParcel.setSourceStreet(payload.sourceStreet());
            savedParcel.setSourceHouseNumber(payload.sourceHouseNumber());
            savedParcel.setSourceFlatNumber(payload.sourceFlatNumber());
            savedParcel.setReceiver(receiver);
            savedParcel.setDestinationCountry(payload.destinationCountry());
            savedParcel.setDestinationCity(payload.destinationCity());
            savedParcel.setDestinationStreet(payload.destinationStreet());
            savedParcel.setDestinationHouseNumber(payload.destinationHouseNumber());
            savedParcel.setDestinationFlatNumber(payload.destinationFlatNumber());
            savedParcel.setAmount(payload.amount());
            savedParcel.setPrice(payload.price());
            savedParcel.setEstimatedPicKUpDate(payload.estimatedPicKUpDate());
            savedParcel.setRequest(false);
            savedParcel.setAccept(true);

            var newParcel = repository.save(savedParcel);
            telegramNotificationService.sendTelegramAcceptParcelNotification(newParcel);
            return newParcel;
        }
        return null;
    }

    public Parcel reject(Long parcelId) {
        var savedParcel = getById(parcelId);

        if (savedParcel != null) {
            savedParcel.setRequest(false);
            savedParcel.setAccept(false);
            var newParcel = repository.save(savedParcel);
            telegramNotificationService.sendTelegramRejectParcelNotification(newParcel);
            return newParcel;
        }
        return null;
    }

    public Route getRouteById(Long routeId) {
        Route route = routeService.getById(routeId);
        if (route == null) {
            log.error("Rout was nat found by id: {}", routeId);
            throw new IllegalArgumentException("Rout was nat found by id: " + routeId);
        }
        return route;
    }

    private Account getAccount(String phoneNumber, String firstName, String lastName) {
        if (phoneNumber != null && firstName != null && lastName != null) {
            return accountService.getAccountByPhoneNumber(phoneNumber).orElseGet(() -> accountService.createWithoutPassword(phoneNumber, firstName, lastName));
        }
        return null;
    }

    public List<Parcel> getAllByRoute(Route route) {
        return repository.findByRoute(route);
    }

    public List<Parcel> getAllBySenderPhoneNumber(String senderPhoneNumber) {
        return repository.findAllBySender_PhoneNumber(senderPhoneNumber);
    }

    public Parcel getById(Long parcelId) {
        return repository.getReferenceById(parcelId);
    }

    public List<Parcel> getAllReceiverParcel(String phoneNumber) {
        var receiver = accountService.getAccountByPhoneNumber(phoneNumber);
        return repository.getAllByReceiver(receiver.get());
    }
}
