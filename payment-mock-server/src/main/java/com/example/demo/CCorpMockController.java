package com.example.demo;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/c-corp")
@RequiredArgsConstructor
public class CCorpMockController {

    private final WebhookService webhookService;

    @PostMapping("/payments/virtual-account")
    public Map<String, Object> createVirtualAccount(@RequestBody Map<String, Object> request) {
        String reservationId = (String) request.get("reservationId");
        Integer amount = (Integer) request.get("amount");
        String vaId = "C_VA_" + UUID.randomUUID();

        webhookService.sendPaymentWebhookAsync(reservationId, "C", vaId, amount.longValue());

        return Map.of(
                "va_id", vaId,
                "va_number", "987654321",
                "status", "SUCCESS",
                "message", "C사: 가상계좌 발급 및 입금 완료 처리."
        );
    }
}
