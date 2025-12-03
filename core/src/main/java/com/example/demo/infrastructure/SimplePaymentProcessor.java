package com.example.demo.infrastructure;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.demo.domain.PaymentProviderType;
import com.example.demo.service.PaymentProcessor;
import com.example.demo.service.PaymentResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class SimplePaymentProcessor implements PaymentProcessor {

    private final RestClient restClient;

    @Override
    public PaymentProviderType getProviderType() {
        return PaymentProviderType.SIMPLE;
    }

    @Override
    public PaymentResult process(Long reservationId, int amount) {
        log.info("간편 결제 처리: 예약 ID = {}, 금액 = {}", reservationId, amount);

        Map<String, Object> request = Map.of(
                "reservationId", reservationId,
                "amount", amount
        );

        Map<String, Object> response = restClient.post()
                .uri("/api/b-corp/pay")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        return new PaymentResult(
                response.get("result").equals("SUCCESS") ? true : false,
                (String) response.get("result"),
                (String) response.get("pay_key"),
                amount,
                LocalDateTime.now()
        );
    }
}
