package com.kpi.routetracker.controller.parcel;

import com.kpi.routetracker.controller.payload.NewParcelPayload;
import com.kpi.routetracker.controller.payload.UpdateParcelPayload;
import com.kpi.routetracker.dto.ParcelDto;
import com.kpi.routetracker.services.parcel.ParcelService;
import com.kpi.routetracker.utils.mapper.ParcelMapper;
import com.kpi.routetracker.utils.token.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("${endpoint.api.root}/parcels")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParcelsController {

    ParcelService parcelService;

    JwtUtils jwtUtils;

    @GetMapping()
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(ParcelMapper.mapper.toDto(parcelService.getAll()));
    }

    @GetMapping("/")
    public ResponseEntity<?> getSpecifiedParcels(@RequestParam Long routeId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date estimatedPickUp,
                                                 HttpServletRequest request) {
        String jwtToken = request.getHeader("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(ParcelMapper.mapper.toDto(parcelService.getSpecifiedParcels(routeId, estimatedPickUp, jwtUtils.extractUsername(jwtToken))));
    }

    @PostMapping()
    public ResponseEntity<?> crate(@Valid @RequestBody NewParcelPayload payload, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder)
            throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        ParcelDto parcel = ParcelMapper.mapper.toDto(parcelService.create(payload));
        return ResponseEntity.created(uriComponentsBuilder.replacePath("route-tracker/parcels/{parcelId}").build(Map.of("parcelId", parcel.getId()))).body(parcel);

    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateParcelPayload payload, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder)
            throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok(ParcelMapper.mapper.toDto(parcelService.update(payload)));
    }

    //    @PostMapping()
    //    public ResponseEntity<?> getAllB(@Valid @RequestBody NewParcelPayload payload, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder)
    //            throws BindException {
    //        if (bindingResult.hasErrors()) {
    //            throw new BindException(bindingResult);
    //        }
    //        ParcelDto parcel = ParcelMapper.mapper.toDto(parcelService.create(payload));
    //        return ResponseEntity.created(uriComponentsBuilder.replacePath("route-tracker/parcels/{parcelId}").build(Map.of("parcelId", parcel.getId()))).body(parcel);
    //
    //    }

}
