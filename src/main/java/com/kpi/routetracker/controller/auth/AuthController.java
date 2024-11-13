package com.kpi.routetracker.controller.auth;

import com.kpi.routetracker.controller.payload.AuthenticationPayload;
import com.kpi.routetracker.services.account.AccountService;
import com.kpi.routetracker.utils.token.JwtUtils;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
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

import java.util.HashMap;

@AllArgsConstructor
@RestController
@RequestMapping("${endpoint.api.root}/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthController {

    final AccountService accountService;
    final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationPayload payload, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok(jwtUtils.generateToke(accountService.getAccountByPhoneNumber(payload.login()).orElseThrow(NotFoundException::new), new HashMap<>()));
    }
}
