package com.kpi.routetracker.controller.route;

import com.kpi.routetracker.services.route.RouteService;
import com.kpi.routetracker.utils.mapper.RouteMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("${endpoint.api.root}/routes/{id:\\d+}")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteController {

    RouteService service;

    @PutMapping("/share")
    public ResponseEntity<?> shareRoute(@PathVariable("id") Long id, @RequestParam("newOwnerPhoneNumber") String newOwnerPhoneNumber) {
        return ResponseEntity.ok(RouteMapper.mapper.toDto(service.shareRoute(id, newOwnerPhoneNumber)));
    }

    @PutMapping("/hire/worker")
    public ResponseEntity<?> hireWorkerOnRoute(@PathVariable("id") Long id, @RequestParam("workerPhoneNumber") String workerPhoneNumber) {
        return ResponseEntity.ok(RouteMapper.mapper.toDto(service.hireWorkerOnRoute(id, workerPhoneNumber)));
    }

    @PutMapping("/leave/worker")
    public ResponseEntity<?> leaveWorkerOnRoute(@PathVariable("id") Long id, @RequestParam("workerPhoneNumber") String workerPhoneNumber) {
        service.leaveWorkerOnRoute(id, workerPhoneNumber);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/hire/car")
    public ResponseEntity<?> hireCarOnRoute(@PathVariable("id") Long id, @RequestParam("carNumber") String carNumber) {
        return ResponseEntity.ok(RouteMapper.mapper.toDto(service.hireCarOnRoute(id, carNumber)));
    }

    @PutMapping("/leave/car")
    public ResponseEntity<?> leaveCarOnRoute(@PathVariable("id") Long id, @RequestParam("carNumber") String carNumber) {
        service.liveCarOnRoute(id, carNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(RouteMapper.mapper.toDto(service.getById(id)));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
