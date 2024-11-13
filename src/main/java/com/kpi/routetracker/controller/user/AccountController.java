package com.kpi.routetracker.controller.user;

import com.kpi.routetracker.dto.AccountDto;
import com.kpi.routetracker.services.account.AccountService;
import com.kpi.routetracker.utils.mapper.AccountMapper;
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
@RequestMapping("${endpoint.api.root}/accounts/{phoneNumber:\\d+}")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountController {

    AccountService accountService;

    @ModelAttribute
    private AccountDto findAccount(@PathVariable("phoneNumber") String phoneNumber) {
        return AccountMapper.mapper.toDto(
                accountService.getAccountByPhoneNumber(phoneNumber).orElseThrow(() -> new NoSuchElementException("Account with number: " + phoneNumber + "not found")));
    }

    @GetMapping
    public ResponseEntity<AccountDto> getByPhoneNumber(@ModelAttribute AccountDto account) {
        return ResponseEntity.ok(account);
    }
}
