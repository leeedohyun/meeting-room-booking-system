package com.example.demo.service;

import static com.example.demo.exception.ErrorCode.PAYMENT_PROVIDER_NOT_SUPPORTED;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Payment;
import com.example.demo.domain.PaymentProviderType;
import com.example.demo.domain.PaymentStatus;
import com.example.demo.domain.Reservation;
import com.example.demo.domain.ReservationStatus;
import com.example.demo.exception.CoreException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final List<PaymentProcessor> paymentProcessors;

    @Transactional(readOnly = true)
    public PaymentStatus getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> CoreException.warn(HttpStatus.NOT_FOUND, ErrorCode.PAYMENT_NOT_FOUND));
        return payment.getStatus();
    }

    @Transactional
    public Long pay(Long reservationId, PaymentProviderType provider, int amount) {
        Payment payment = Payment.pending(amount, reservationId);
        paymentRepository.save(payment);

        PaymentProcessor paymentProcessor = paymentProcessors.stream()
                .filter(p -> p.getProviderType() == provider)
                .findFirst()
                .orElseThrow(() -> CoreException.warn(HttpStatus.BAD_REQUEST, PAYMENT_PROVIDER_NOT_SUPPORTED));
        PaymentResult result = paymentProcessor.process(reservationId, amount);

        payment.updateExternalPaymentId(result.transactionId());
        return payment.getId();
    }

    @Transactional
    public void success(String provider, Long reservationId, String externalPaymentId, int amount) {
        PaymentProviderType providerType = PaymentProviderType.fromProviderCode(provider);
        Reservation reservation = reservationRepository.findByIdAndStatus(reservationId, ReservationStatus.PAYMENT_PENDING)
                .orElseThrow(() -> CoreException.warn(HttpStatus.NOT_FOUND, ErrorCode.RESERVATION_NOT_FOUND));
        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> CoreException.warn(HttpStatus.NOT_FOUND, ErrorCode.PAYMENT_NOT_FOUND));

        if (payment.isNotEqualsAmount(amount)) {
            throw CoreException.warn(HttpStatus.BAD_REQUEST, ErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        payment.success(providerType, externalPaymentId);
        reservation.confirm();
    }
}
