package com.kpi.routetracker.telegram;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class MessageUtils {

    public static void sendMessage(long chatId, String textToSend, TelegramLongPollingBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message: ", e);
        }
    }

    public static void sendLocation(long chatId, double latitude, double longitude, TelegramLongPollingBot bot) {
        SendLocation location = new SendLocation();
        location.setChatId(String.valueOf(chatId));
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        try {
            bot.execute(location);
        } catch (TelegramApiException e) {
            log.error("Error sending location: ", e);
        }
    }
}
