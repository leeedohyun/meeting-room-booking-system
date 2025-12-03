package com.example.demo.service;

import static com.example.demo.exception.ErrorCode.PAYMENT_PROVIDER_NOT_SUPPORTED;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Payment;
import com.example.demo.domain.PaymentProviderType;
import com.example.demo.domain.PaymentStatus;
import com.example.demo.exception.CoreException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
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
    public void success(String provider, Long reservationId, String externalPaymentId) {
        PaymentProviderType providerType = PaymentProviderType.fromProviderCode(provider);
        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> CoreException.warn(HttpStatus.NOT_FOUND, ErrorCode.PAYMENT_NOT_FOUND));
        payment.success(providerType, externalPaymentId);
    }
}
