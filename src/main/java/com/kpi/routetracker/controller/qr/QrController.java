package com.kpi.routetracker.controller.qr;

import com.google.zxing.WriterException;
import com.kpi.routetracker.services.qr.QrCodeGeneratorService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("${endpoint.api.root}/qr")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QrController {

    QrCodeGeneratorService qrCodeGeneratorService;

    @GetMapping
    public ResponseEntity<byte[]> generateQrCode(@RequestParam String phoneNumber, @RequestParam int width, @RequestParam int height)
            throws IOException, WriterException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrCodeGeneratorService.generateQrCodeWithPhoneNumber(phoneNumber, width, height));
    }
}
