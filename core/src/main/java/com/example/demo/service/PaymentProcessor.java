package com.example.demo.service;

import com.example.demo.domain.PaymentProviderType;

public interface PaymentProcessor {

    PaymentProviderType getProviderType();

    PaymentResult process(Long reservationId, int amount);
}
