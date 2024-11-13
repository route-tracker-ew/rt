package com.kpi.routetracker.controller.car;

import com.kpi.routetracker.controller.payload.NewCarPayload;
import com.kpi.routetracker.services.route.CarService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${endpoint.api.root}/cars")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarsController {

    CarService service;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody NewCarPayload payload, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok(service.create(payload));
    }

}
