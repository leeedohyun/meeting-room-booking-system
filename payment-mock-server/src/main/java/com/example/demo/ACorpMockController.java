package com.example.demo;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/a-corp")
@RequiredArgsConstructor
public class ACorpMockController {

    private final WebhookService webhookService;

    @PostMapping("/payments")
    public ResponseEntity<?> authorizePayment(@RequestBody Map<String, Object> request) {
        Integer amount = (Integer) request.get("amount");
        String reservationId = (String) request.get("reservationId");
        String transactionId = UUID.randomUUID().toString();

        if (amount == null || amount < 100) {
            return ResponseEntity.badRequest().body(
                    Map.of("transaction_id", "A_FAIL_" + reservationId, "status_code", 400, "message", "A사: 금액 오류")
            );
        }

        webhookService.sendPaymentWebhookAsync(reservationId, "A", transactionId, amount.longValue());

        return ResponseEntity.ok(
                Map.of(
                        "transaction_id", transactionId,
                        "status_code", HttpStatus.OK.value(),
                        "message", "A사: 승인 완료",
                        "approved_amount", amount,
                        "approved_at", LocalDateTime.now()
                )
        );
    }
}
