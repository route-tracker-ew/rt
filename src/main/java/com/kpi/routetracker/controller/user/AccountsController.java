package com.kpi.routetracker.controller.user;

import com.kpi.routetracker.controller.payload.NewAccountPayload;
import com.kpi.routetracker.controller.payload.NewGpsTrackerPayload;
import com.kpi.routetracker.services.account.AccountService;
import com.kpi.routetracker.utils.mapper.AccountMapper;
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

@AllArgsConstructor
@RestController
@RequestMapping("${endpoint.api.root}/accounts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountsController {

    AccountService accountService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody NewAccountPayload payload, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok(AccountMapper.mapper.toDto(accountService.create(payload)));
    }

    @PostMapping("/gps")
    public ResponseEntity<?> createGps(@Valid @RequestBody NewGpsTrackerPayload payload, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok(AccountMapper.mapper.toDto(accountService.createGps(payload)));
    }
}
