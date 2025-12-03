package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import org.springframework.http.HttpStatus;

import com.example.demo.exception.CoreException;
import com.example.demo.exception.ErrorCode;

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

    public static Payment pending(int amount, Long reservationId) {
        return new Payment(null, amount, PaymentStatus.PENDING, null, reservationId);
    }

    public void success(PaymentProviderType providerType, String externalPaymentId) {
        if (status != PaymentStatus.PENDING) {
            throw CoreException.warn(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_PAYMENT_STATUS);
        }
        this.providerType = providerType;
        this.status = PaymentStatus.SUCCESS;
        this.externalPaymentId = externalPaymentId;
    }

    public boolean isNotEqualsAmount(int amount) {
        return this.amount != amount;
    }

    public void updateExternalPaymentId(String externalPaymentId) {
        this.externalPaymentId = externalPaymentId;
    }
}
