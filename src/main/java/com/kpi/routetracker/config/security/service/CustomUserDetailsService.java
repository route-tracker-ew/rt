package com.kpi.routetracker.config.security.service;

import com.kpi.routetracker.config.security.user.CustomUserDetails;
import com.kpi.routetracker.model.user.Account;
import com.kpi.routetracker.services.account.AccountService;
import jakarta.ws.rs.NotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CustomUserDetailsService {

    final AccountService service;

    public CustomUserDetails loadUserByUsername(String login) {
        Account account = service.getAccountByPhoneNumber(login).orElseThrow(NotFoundException::new);
        return new CustomUserDetails(account.getPhoneNumber(), account.getPassword(), new Object(), true, true, true, true);
    }

}
