package com.kpi.routetracker.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.WriterException;
import com.kpi.routetracker.config.telegram.BotConfig;
import com.kpi.routetracker.telegram.service.TelegramQrCodeService;
import com.kpi.routetracker.telegram.service.TelegramParcelService;
import com.kpi.routetracker.telegram.service.TelegramAccountManagementService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    final TelegramParcelService telegramParcelService;
    final TelegramQrCodeService telegramQrCodeService;
    final TelegramAccountManagementService telegramAccountManagementService;
    static final List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "get a welcome message"),
            new BotCommand("/help", "info on how to use this bot"),
            new BotCommand("/findparcelbyphone", "track your parcel"),
            new BotCommand("/findallparcels", "find all your parcels"),
            new BotCommand("/createparcels", "create parcel"),
            new BotCommand("/removeparcels", "remove parcels"),
            new BotCommand("/register", "register as a user"),
            new BotCommand("/getqr", "get your QR code")
    );

    static final String HELP_TEXT = "Цей бот допоможе відстежити вашу посилку.";

    public TelegramBot(BotConfig config, TelegramParcelService telegramParcelService, TelegramQrCodeService telegramQrCodeService,
                       TelegramAccountManagementService telegramAccountManagementService) throws TelegramApiException {
        this.config = config;
        this.telegramParcelService = telegramParcelService;
        this.telegramQrCodeService = telegramQrCodeService;
        this.telegramAccountManagementService = telegramAccountManagementService;

        // Add commands to bot menu
        this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            telegramParcelService.handleParcelCreation(chatId, callbackData, this);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (telegramAccountManagementService.isRegistrationStarted(chatId)) {
                telegramAccountManagementService.handleRegistration(chatId, messageText, this);
            } else if (telegramParcelService.isParcelCreationStarted(chatId)) {
                telegramParcelService.handleParcelCreation(chatId, messageText, this);
            }else if (telegramParcelService.isParcelDeletingStarted(chatId)) {
                telegramParcelService.continueDeletingParcel(chatId, messageText, this);
            } else {
                switch (messageText) {
                    case "/start":
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    case "/help":
                        MessageUtils.sendMessage(chatId, HELP_TEXT, this);
                        break;
                    case "/findparcelbyphone":
                        MessageUtils.sendMessage(chatId, "Будь ласка, введіть свій номер телефону для відстеження посилки:", this);
                        break;
                    case "/findallparcels":
                        try {
                            telegramParcelService.findAllParcels(chatId, this);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "/register":
                        telegramAccountManagementService.startUserRegistration(chatId, this);
                        break;
                    case "/getqr":
                        try {
                            telegramQrCodeService.sendQrCode(chatId, this);
                        } catch (IOException | WriterException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "/createparcels":
                        telegramParcelService.startParcelCreating(chatId, this);
                        break;
                    case "/removeparcels":
                        telegramParcelService.startDeletingParcel(chatId, this);
                        break;
                    default:
                        if (messageText.matches("\\+?[0-9]{10,15}")) {
                            // Handle phone number input
                            try {
                                telegramParcelService.findParcels(chatId, messageText, this);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            MessageUtils.sendMessage(chatId, "Вибачте, команду не розпізнано", this);
                        }
                }
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Привіт, " + name + ", приємно познайомитися!";
        MessageUtils.sendMessage(chatId, answer, this);
    }

}
