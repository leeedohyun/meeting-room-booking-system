package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private PaymentProviderType providerType;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    private String externalPaymentId;

    @Column(nullable = false)
    private Long reservationId;

    public Payment(int amount, Long reservationId) {
        this(null, amount, PaymentStatus.PENDING, null, reservationId);
    }

    public Payment(
            PaymentProviderType providerType,
            int amount,
            PaymentStatus status,
            String externalPaymentId,
            Long reservationId
    ) {
        this.providerType = providerType;
        this.amount = amount;
        this.status = status;
        this.externalPaymentId = externalPaymentId;
        this.reservationId = reservationId;
    }

    public void success(PaymentProviderType providerType, String externalPaymentId) {
        this.providerType = providerType;
        this.status = PaymentStatus.SUCCESS;
        this.externalPaymentId = externalPaymentId;
    }
}
