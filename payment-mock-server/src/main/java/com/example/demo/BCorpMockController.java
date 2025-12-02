package com.example.demo;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/b-corp")
@RequiredArgsConstructor
public class BCorpMockController {

    private final WebhookService webhookService;

    @PostMapping("/pay")
    public Map<String, Object> simplePay(@RequestBody Map<String, Object> request) {
        String reservationId = (String) request.get("reservationId");
        Integer totalAmount = (Integer) request.get("amount");
        String transactionId = "B_KEY_" + UUID.randomUUID();

        if (totalAmount != null && totalAmount > 500000) {
            return Map.of("pay_key", reservationId, "result", "FAIL",
                    "error", Map.of("code", "B101", "message", "B사: 한도 초과"));
        }

        assert totalAmount != null;
        webhookService.sendPaymentWebhookAsync(reservationId, "B", transactionId, totalAmount.longValue());

        return Map.of(
                "pay_key", transactionId,
                "result", "SUCCESS",
                "error", null
        );
    }
}
