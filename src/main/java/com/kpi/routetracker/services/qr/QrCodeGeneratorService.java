package com.kpi.routetracker.services.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.kpi.routetracker.services.account.AccountService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QrCodeGeneratorService {

    AccountService accountService;

    private byte[] makeQrCode(String phoneNumber, Integer width, Integer height) throws WriterException, IOException {
        Date date = new Date();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(phoneNumber, BarcodeFormat.QR_CODE, width, height, hints);

        MatrixToImageConfig config = new MatrixToImageConfig(Colors.BLACK.getArgb(), Colors.WHITE.getArgb());
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config);

        BufferedImage logoImage = ImageIO.read(new File("src/main/resources/icon.png"));

        int deltaHeight = qrImage.getHeight() - logoImage.getHeight();
        int deltaWidth = qrImage.getWidth() - logoImage.getWidth();

        BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) combined.getGraphics();
        g.drawImage(qrImage, 0, 0, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g.drawImage(logoImage, Math.round(deltaWidth / 2), Math.round(deltaHeight / 2), null);

        // Записуємо зображення в байтовий масив
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(combined, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        log.info("done");

        return imageBytes;
    }

    public byte[] generateQrCodeWithPhoneNumber(String phoneNumber, Integer width, Integer height) throws IOException, WriterException {
        var account = accountService.getAccountByPhoneNumber(phoneNumber);
        if (account.isEmpty()) {
            throw new IllegalArgumentException("User with phoneNumber: " + phoneNumber + " not found");
        }
        return makeQrCode(account.get().getPhoneNumber(), width, height);
    }

}
