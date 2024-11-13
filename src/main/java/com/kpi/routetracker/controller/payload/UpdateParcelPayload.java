package com.kpi.routetracker.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record UpdateParcelPayload(
        Long id,

        String senderPhoneNumber,

        String senderLastName,

        String senderFirstName,

        String sourceCountry,

        String sourceCity,

        String sourceStreet,

        String sourceHouseNumber,

        Integer sourceFlatNumber,

        @NotNull(message = "Receiver phone number field should not be null")
        @NotEmpty(message = "Receiver phone number field should not be empty")
        String receiverPhoneNumber,

        @NotNull(message = "Receiver first name field should not be null")
        @NotEmpty(message = "Receiver first name field should not be empty")
        String receiverFirstName,

        @NotNull(message = "Receiver last name field should not be null")
        @NotEmpty(message = "Receiver last name field should not be empty")
        String receiverLastName,

        @NotNull(message = "Destination country field should not be null")
        @NotEmpty(message = "Destination city field should not be empty")
        String destinationCountry,
        @NotNull(message = "Destination city field should not be null")
        @NotEmpty(message = "Destination city field should not be empty")
        String destinationCity,
        String destinationStreet,

        String destinationHouseNumber,

        Integer destinationFlatNumber,

        Date estimatedPicKUpDate,

        @NotNull(message = "Amount field should not be null")
        @Min(value = 1, message = "Amount field should be grater than 0")
        Integer amount,
        //        DeliveryService deliveryService,

        Integer destinationPostNumber,
        Double price,

        Double weight) {

}
