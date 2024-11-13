package com.kpi.routetracker.controller.parcel;

import com.kpi.routetracker.dto.AccountDto;
import com.kpi.routetracker.dto.ParcelDto;
import com.kpi.routetracker.services.parcel.ParcelService;
import com.kpi.routetracker.utils.mapper.AccountMapper;
import com.kpi.routetracker.utils.mapper.ParcelMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("${endpoint.api.root}/parcels/{id:\\d+}")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParcelController {

    private ParcelService parcelService;

    @GetMapping
    public ResponseEntity<ParcelDto> getById(@PathVariable("id") Long parcelId) {
        var r = parcelService.getById(parcelId);
        System.out.println(r.getDestinationCountry());
        var r2 = ParcelMapper.mapper.toDto(r);
        r2.setDestinationCountry(r.getDestinationCountry());
        return ResponseEntity.ok(r2);
    }
}
