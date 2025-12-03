package com.example.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.example.demo.exception.CoreException;
import com.example.demo.exception.ErrorCode;

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

    @Test
    void successFail() {
        // given
        Payment payment = new Payment();
        payment.success(PaymentProviderType.CARD, "ext123");

        // when & then
        assertThatThrownBy(() -> payment.success(PaymentProviderType.CARD, "ext456"))
                .isInstanceOf(CoreException.class)
                .hasMessage(ErrorCode.INVALID_PAYMENT_STATUS.getMessage());
    }
}
