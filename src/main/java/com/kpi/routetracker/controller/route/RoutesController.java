package com.kpi.routetracker.controller.route;

import com.kpi.routetracker.controller.payload.NewRoutePayload;
import com.kpi.routetracker.dto.RouteDto;
import com.kpi.routetracker.services.route.RouteService;
import com.kpi.routetracker.utils.mapper.RouteMapper;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("${endpoint.api.root}/routs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoutesController {

    RouteService routeService;

    @GetMapping
    public ResponseEntity<List<RouteDto>> getAll() {
        return ResponseEntity.ok(RouteMapper.mapper.toDto(routeService.getAll()));
    }

    @GetMapping("/owners/{ownerPhoneNumber:\\d+}")
    public ResponseEntity<?> getByOwnerNumber(@PathVariable("ownerPhoneNumber") String ownerPhoneNumber) {
        return ResponseEntity.ok(RouteMapper.mapper.toDto(routeService.getByOwnerPhoneNumber(ownerPhoneNumber)));
    }

    @GetMapping("/workers/{workerPhoneNumber:\\d+}")
    public ResponseEntity<?> getByWorkerNumber(@PathVariable("workerPhoneNumber") String workerPhoneNumber) {
        return ResponseEntity.ok(RouteMapper.mapper.toDto(routeService.getByWorkerPhoneNumber(workerPhoneNumber)));
    }


    @PostMapping()
    public ResponseEntity<?> crate(@Valid @RequestBody NewRoutePayload payload, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder)
            throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        RouteDto route = RouteMapper.mapper.toDto(routeService.create(payload));
        return ResponseEntity.created(uriComponentsBuilder.replacePath("route-tracker/routs/{routeId}").build(Map.of("routeId", route.getId()))).body(route);

    }
}
