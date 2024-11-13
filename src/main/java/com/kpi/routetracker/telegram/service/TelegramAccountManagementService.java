package com.kpi.routetracker.telegram.service;

import com.kpi.routetracker.model.user.TelegramAccount;
import com.kpi.routetracker.services.account.AccountService;
import com.kpi.routetracker.services.account.TelegramAccountService;
import com.kpi.routetracker.telegram.MessageUtils;
import com.kpi.routetracker.telegram.model.user.TelegramUserRegistrationData;
import com.kpi.routetracker.telegram.model.user.TelegramUserRegistrationState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramAccountManagementService {

    // Map to store registration state per user
    final Map<Long, TelegramUserRegistrationState> userRegistrationStateMap = new HashMap<>();
    final Map<Long, TelegramUserRegistrationData> userRegistrationDataMap = new HashMap<>();
    final AccountService accountService;
    final TelegramAccountService telegramAccountService;

    public void startUserRegistration(long chatId, TelegramLongPollingBot bot) {
        userRegistrationStateMap.put(chatId, TelegramUserRegistrationState.ENTER_PHONE_NUMBER);
        userRegistrationDataMap.put(chatId, new TelegramUserRegistrationData());
        MessageUtils.sendMessage(chatId, "Будь ласка, введіть свій номер телефону:", bot);
    }

    public void handleRegistration(long chatId, String messageText, TelegramLongPollingBot bot) {
        TelegramUserRegistrationState currentState = userRegistrationStateMap.get(chatId);
        TelegramUserRegistrationData registrationData = userRegistrationDataMap.get(chatId);

        switch (currentState) {
            case ENTER_PHONE_NUMBER:
                registrationData.setPhoneNumber(messageText);
                userRegistrationStateMap.put(chatId, TelegramUserRegistrationState.ENTER_FIRST_NAME);
                MessageUtils.sendMessage(chatId, "Будь ласка, введіть своє ім'я:", bot);
                break;

            case ENTER_FIRST_NAME:
                registrationData.setFirstName(messageText);
                userRegistrationStateMap.put(chatId, TelegramUserRegistrationState.ENTER_LAST_NAME);
                MessageUtils.sendMessage(chatId, "Будь ласка, введіть своє прізвище:", bot);
                break;

            case ENTER_LAST_NAME:
                registrationData.setLastName(messageText);
                userRegistrationStateMap.put(chatId, TelegramUserRegistrationState.COMPLETED);
                completeRegistration(chatId, registrationData, bot);
                break;

            case COMPLETED:
                // Registration already completed
                MessageUtils.sendMessage(chatId, "Реєстрація вже завершена.", bot);
                break;
        }
    }

    public boolean isRegistrationStarted(Long chatId) {
        return userRegistrationStateMap.containsKey(chatId);
    }

    private void completeRegistration(long chatId, TelegramUserRegistrationData registrationData, TelegramLongPollingBot bot) {
        // Save the user to the database or perform other registration logic
        var result = createTelegramAccount(registrationData, chatId);

        // Clear the registration state
        userRegistrationStateMap.remove(chatId);
        userRegistrationDataMap.remove(chatId);

        // Notify the user of successful registration
        if (result == null) {
            MessageUtils.sendMessage(chatId, "Користувач уже зареєстрований!", bot);
        } else {
            MessageUtils.sendMessage(chatId, "Реєстрація завершена! Ласкаво просимо, " + registrationData.getFirstName() + "!", bot);
        }
    }

    public TelegramAccount createTelegramAccount(TelegramUserRegistrationData registrationData, Long chatId) {
        if (telegramAccountService.getByChatId(chatId).isEmpty()) {
            var account = accountService.getAccountByPhoneNumber(registrationData.getPhoneNumber());
            if (account.isEmpty()) {
                var newAccount = accountService.createWithoutPassword(registrationData.getPhoneNumber(), registrationData.getFirstName(), registrationData.getLastName());
                return telegramAccountService.create(newAccount, chatId);
            } else {
                return telegramAccountService.create(account.get(), chatId);
            }
        }
        return null;
    }

    public TelegramAccount getTelegramAccount(Long chatId) {
        return telegramAccountService.getByChatId(chatId).orElse(null);
    }
}
