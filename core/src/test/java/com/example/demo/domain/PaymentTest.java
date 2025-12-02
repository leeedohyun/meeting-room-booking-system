package com.example.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void success() {
        // given
        Payment payment = new Payment();

        // when
        payment.success(PaymentProviderType.CARD, "ext123");

        // then
        assertThat(payment.getProviderType()).isEqualTo(PaymentProviderType.CARD);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(payment.getExternalPaymentId()).isEqualTo("ext123");
    }
}
