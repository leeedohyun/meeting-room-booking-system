package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

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
    ReservationService reservationService;

    @Autowired
    PaymentService paymentService;

    @Test
    void success() {
        // given
        var userId = 1L;
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 0);
        var endTime = LocalDateTime.of(2025, 11, 29, 18, 30);
        var reservationId = reservationService.reserve(userId, meetingRoomId, startTime, endTime);
        paymentRepository.save(new Payment(10000, reservationId));

        // when
        paymentService.success("A", reservationId, "external-payment-id-12345");

        // then
        var payment = paymentRepository.findByReservationId(reservationId).orElseThrow();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(payment.getProviderType()).isEqualTo(PaymentProviderType.CARD);
        assertThat(payment.getExternalPaymentId()).isEqualTo("external-payment-id-12345");
    }

    @Test
    void success_WhenPaymentNotFound() {
        // given
        var reservationId = 9999L;

        // when & then
        assertThatThrownBy(() -> paymentService.success("A", reservationId, "external-payment-id-12345"))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.PAYMENT_NOT_FOUND.getMessage());
    }

    @Test
    void getPaymentStatus() {
        // given
        var userId = 1L;
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 0);
        var endTime = LocalDateTime.of(2025, 11, 29, 18, 30);
        var reservationId = reservationService.reserve(userId, meetingRoomId, startTime, endTime);
        var payment = paymentRepository.save(
                new Payment(
                        PaymentProviderType.CARD,
                        1000,
                        PaymentStatus.PENDING,
                        "external-payment-id-12345",
                        reservationId
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

    @Test
    void pay() {
        // given
        var userId = 1L;
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 0);
        var endTime = LocalDateTime.of(2025, 11, 29, 18, 30);
        var reservationId = reservationService.reserve(userId, meetingRoomId, startTime, endTime);
        var amount = 10000;

        // when
        var paymentId = paymentService.pay(reservationId, PaymentProviderType.CARD, amount);

        // then
        var paymentStatus = paymentService.getPaymentStatus(paymentId);
        assertThat(paymentStatus).isEqualTo(PaymentStatus.PENDING);
    }
}
