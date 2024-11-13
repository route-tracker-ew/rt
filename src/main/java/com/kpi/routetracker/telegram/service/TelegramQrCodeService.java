package com.kpi.routetracker.telegram.service;

import com.google.zxing.WriterException;
import com.kpi.routetracker.services.account.TelegramAccountService;
import com.kpi.routetracker.services.qr.QrCodeGeneratorService;
import com.kpi.routetracker.telegram.MessageUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramQrCodeService {

    final QrCodeGeneratorService qrCodeGeneratorService;
    final TelegramAccountService telegramAccountService;

    public void sendQrCode(long chatId, TelegramLongPollingBot bot) throws IOException, WriterException {
        var telegramAccount = telegramAccountService.getByChatId(chatId);
        if (telegramAccount.isPresent()) {
            // Generate QR code (for example, with phone number)
            byte[] qrCodeImage = qrCodeGeneratorService.generateQrCodeWithPhoneNumber("0631536533", 1000, 1000);

            // Save QR code as file
            File qrFile = saveQrCodeToFile(qrCodeImage, "qr_code.png");

            // Send QR code as photo
            sendQrCodeAsPhoto(chatId, qrFile, bot);

            // Delete temporary file after sending (optional)
            if (qrFile.exists()) {
                qrFile.delete();
            }
        } else {
            MessageUtils.sendMessage(chatId, "Схоже на те що Ви не зареєстровані у системі, через це ми не можемо згенерувати Вам qr-code", bot);
        }

    }

    private File saveQrCodeToFile(byte[] qrCodeImage, String fileName) throws IOException {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(qrCodeImage);
        }
        return file;
    }

    private void sendQrCodeAsPhoto(long chatId, File qrFile, TelegramLongPollingBot bot) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(String.valueOf(chatId));
        sendPhotoRequest.setPhoto(new InputFile(qrFile));
        sendPhotoRequest.setCaption("Ваш QR-код. Покажіть його оператору, щоб він міг знайти Ваші посилки.");

        try {
            bot.execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            log.error("Error sending QR code photo: ", e);
        }
    }
}
