package com.example.demo;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final RestClient restClient;
    private final String mainServerBaseUrl = "http://localhost:8080/webhooks/payments/";

    @Async
    public void sendPaymentWebhookAsync(String reservationId, String providerCode, String transactionId, Long amount) {
        try {
            StandardPaymentUpdateDto payload = new StandardPaymentUpdateDto(
                    reservationId,
                    "SUCCESS",
                    transactionId,
                    providerCode,
                    amount
            );

            String webhookUrl = mainServerBaseUrl + providerCode;

            restClient.post()
                    .uri(webhookUrl)
                    .body(payload)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            (request, response) -> {
                                throw new RuntimeException(
                                        "HTTP Error: " + response.getStatusCode() + " " + response.getStatusText()
                                );
                            })
                    .toBodilessEntity();

        } catch (Exception e) {
            log.error("Mock Server: Webhook FAILED for {}. Error: {}", providerCode, e.getMessage(), e);
        }
    }
}
