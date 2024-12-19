package com.kpi.routetracker.telegram.model.parcel;

import com.kpi.routetracker.model.parcel.Parcel;
import com.kpi.routetracker.model.parcel.ParcelStatus;
import com.kpi.routetracker.model.route.Route;
import com.kpi.routetracker.model.user.Account;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramParcelCreationDate {

    String senderPhone;
    String senderName;
    String senderLastName;
    String countrySender;
    String citySender;
    String streetSender;
    String houseNumberSender;
    Integer apartmentNumberSender;
    String receiverPhone;
    String receiverName;
    String receiverLastName;
    String countryReceiver;
    String cityReceiver;
    String streetReceiver;
    String houseReceiver;
    Integer apartmentReceiver;
    int parcelCount;

    public Parcel toParcel(Account sender, Account receiver, Route route) {
        return Parcel.builder()
                .sender(sender)
                .sourceCountry(countrySender)
                .sourceCity(citySender)
                .sourceStreet(streetSender)
                .sourceHouseNumber(houseNumberSender)
                .sourceFlatNumber(apartmentNumberSender)
                .receiver(receiver)
                .destinationCountry(countryReceiver)
                .destinationCity(cityReceiver)
                .destinationStreet(streetReceiver)
                .destinationHouseNumber(houseReceiver)
                .destinationFlatNumber(apartmentReceiver)
                .amount(parcelCount)
                .route(route)
                .parcelStatus(ParcelStatus.CHECKING)
                .request(true)
                .accept(false)
                .build();
    }
}
