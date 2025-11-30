package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Payment;
import com.example.demo.domain.PaymentProviderType;
import com.example.demo.domain.PaymentStatus;
import com.example.demo.exception.CoreException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.PaymentRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PaymentServiceTest {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentService paymentService;

    @Test
    void getPaymentStatus() {
        // given
        var payment = paymentRepository.save(
                new Payment(
                        PaymentProviderType.CARD,
                        1000,
                        PaymentStatus.PENDING,
                        "external-payment-id-12345"
                )
        );

        // when
        var status = paymentService.getPaymentStatus(payment.getId());

        // then
        assertThat(status).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void getPaymentStatus_WhenPaymentNotFound() {
        // given
        var paymentId = 9999L;

        // when & then
        assertThatThrownBy(() -> paymentService.getPaymentStatus(paymentId))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.PAYMENT_NOT_FOUND.getMessage());
    }
}
