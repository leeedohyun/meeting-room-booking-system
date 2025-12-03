package com.example.demo.controller.dto;

import com.example.demo.domain.PaymentProviderType;

public record PaymentRequest(
        PaymentProviderType provider,
        int amount
) {

}
