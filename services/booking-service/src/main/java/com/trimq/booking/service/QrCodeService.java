package com.trimq.booking.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for generating QR codes for booking passes.
 */
@Service
@Slf4j
public class QrCodeService {

    private static final int DEFAULT_WIDTH = 250;
    private static final int DEFAULT_HEIGHT = 250;
    private static final int QR_ON_COLOR = 0xFF1a1a2e;   // Dark navy
    private static final int QR_OFF_COLOR = 0xFFFFFFFF;  // White

    /**
     * Generate QR code as Base64 encoded PNG image.
     * 
     * @param content Content to encode (pass code)
     * @return Base64 encoded PNG image
     */
    public String generateQrCodeBase64(String content) {
        return generateQrCodeBase64(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Generate QR code as Base64 encoded PNG image with custom dimensions.
     * 
     * @param content Content to encode
     * @param width QR code width
     * @param height QR code height
     * @return Base64 encoded PNG image
     */
    public String generateQrCodeBase64(String content, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 2);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageConfig config = new MatrixToImageConfig(QR_ON_COLOR, QR_OFF_COLOR);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream, config);
            
            byte[] imageBytes = outputStream.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(imageBytes);
            
            log.debug("Generated QR code for content: {}", content);
            return "data:image/png;base64," + base64;
            
        } catch (WriterException | IOException e) {
            log.error("Failed to generate QR code for content: {}", content, e);
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    /**
     * Generate QR code as raw bytes.
     * 
     * @param content Content to encode
     * @return PNG image bytes
     */
    public byte[] generateQrCodeBytes(String content) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 2);

            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 
                    DEFAULT_WIDTH, DEFAULT_HEIGHT, hints);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageConfig config = new MatrixToImageConfig(QR_ON_COLOR, QR_OFF_COLOR);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream, config);
            
            return outputStream.toByteArray();
            
        } catch (WriterException | IOException e) {
            log.error("Failed to generate QR code bytes for content: {}", content, e);
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}

