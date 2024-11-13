package com.kpi.routetracker.notification;

import com.kpi.routetracker.dto.TelegramParcelDto;
import com.kpi.routetracker.model.parcel.Parcel;
import com.kpi.routetracker.services.account.TelegramAccountService;
import com.kpi.routetracker.telegram.MessageUtils;
import com.kpi.routetracker.telegram.TelegramBot;
import com.kpi.routetracker.telegram.service.TelegramParcelService;
import com.kpi.routetracker.utils.mapper.TelegramParcelMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramNotificationService {

    final TelegramBot telegramBot;
    final TelegramAccountService telegramAccountService;
    final TelegramParcelService telegramParcelService;

    public void sendTelegramParcelNotification(Parcel parcel) {
        var telegramParcel = TelegramParcelMapper.mapper.toDto(parcel);
        if (parcel.getSender() != null) {
            var sender = telegramAccountService.getChatIdByPhoneNumber(parcel.getSender().getPhoneNumber());
            if (sender != null) {
                sendNotification(sender, telegramParcel, "Ви відправили посилку:\n");
            }
        }
        var receiver = telegramAccountService.getChatIdByPhoneNumber(parcel.getReceiver().getPhoneNumber());
        if (receiver != null) {
            sendNotification(receiver, telegramParcel, "До Вас прямує посилка:\n");
        }
    }

    public void sendNotification(Long chatId, TelegramParcelDto telegramParcelDto, String additionalText) {
        var notification = telegramParcelService.prepareFindParcelResponseText(telegramParcelDto);

        if (additionalText != null && !additionalText.isEmpty()) {
            notification.insert(0, additionalText);
        }
        MessageUtils.sendMessage(chatId, notification.toString(), this.telegramBot);
    }

}
