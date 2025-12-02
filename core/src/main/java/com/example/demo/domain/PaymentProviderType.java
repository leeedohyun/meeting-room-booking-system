package com.example.demo.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentProviderType {

    CARD("A"),
    SIMPLE("B"),
    VIRTUAL_ACCOUNT("C");

    private final String providerCode;

    public static PaymentProviderType fromProviderCode(String externalCode) {
        String code = externalCode.toUpperCase();

        return Arrays.stream(PaymentProviderType.values())
                .filter(type -> type.getProviderCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Unknown provider code: " + externalCode));
    }
}
