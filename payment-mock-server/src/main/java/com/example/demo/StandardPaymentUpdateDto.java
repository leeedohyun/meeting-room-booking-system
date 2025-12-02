package com.example.demo;

public record StandardPaymentUpdateDto(
        String reservationId,
        String status,
        String transactionId,
        String providerCode,
        Long approvedAmount
) {

}
