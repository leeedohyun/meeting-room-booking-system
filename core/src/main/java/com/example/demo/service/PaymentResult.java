package com.example.demo.service;

import java.time.LocalDateTime;

public record PaymentResult(
        boolean success,
        String message,
        String transactionId,
        Integer approvedAmount,
        LocalDateTime approvedAt
) {

}
