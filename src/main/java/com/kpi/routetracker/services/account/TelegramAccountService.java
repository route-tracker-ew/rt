package com.kpi.routetracker.services.account;

import com.kpi.routetracker.model.user.Account;
import com.kpi.routetracker.model.user.TelegramAccount;
import com.kpi.routetracker.repo.TelegramAccountRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramAccountService {

    TelegramAccountRepository repository;

    public Optional<TelegramAccount> getByChatId(Long chatId) {
        return repository.findByTelegramChatId(chatId);
    }

    public TelegramAccount create(Account account, Long chatId) {
        return repository.save(TelegramAccount.builder()
                .account(account)
                .telegramChatId(chatId)
                .build());
    }

    public Long getChatIdByPhoneNumber(String phoneNumber) {
        return repository.findChatIdByPhoneNumber(phoneNumber);
    }
}
