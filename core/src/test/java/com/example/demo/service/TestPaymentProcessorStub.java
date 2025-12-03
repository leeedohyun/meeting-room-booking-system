package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.demo.domain.PaymentProviderType;

@Profile("test")
@Component
public class TestPaymentProcessorStub implements PaymentProcessor {

    @Override
    public PaymentProviderType getProviderType() {
        return PaymentProviderType.CARD;
    }

    @Override
    public PaymentResult process(Long reservationId, int amount) {
        return new PaymentResult(
                true,
                "Payment processed successfully",
                "external-payment-id-12345",
                amount,
                LocalDateTime.now()
        );
    }
}
