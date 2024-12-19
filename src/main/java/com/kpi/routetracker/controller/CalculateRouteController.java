package com.kpi.routetracker.controller;

import com.kpi.routetracker.controller.payload.CalculateRoutePayload;
import com.kpi.routetracker.services.CalculateRouteService;
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
@RequestMapping("${endpoint.api.root}/shortest-route")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculateRouteController {

    CalculateRouteService calculateShortestRouteService;

    @PostMapping()
    public ResponseEntity<?> getRoute(@Valid @RequestBody CalculateRoutePayload payload, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok(calculateShortestRouteService.calculateRoute(payload));
    }

}
