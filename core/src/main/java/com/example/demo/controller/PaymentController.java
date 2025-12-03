package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.PaymentRequest;
import com.example.demo.domain.PaymentStatus;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ReservationService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "결제", description = "결제 관련 API")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/reservations/{id}/payment")
    public void confirm(
            @Parameter(
                    description = "결제할 예약 ID",
                    required = true,
                    in = ParameterIn.PATH,
                    example = "1"
            )
            @PathVariable Long id,
            @RequestBody PaymentRequest request
    ) {
        paymentService.pay(id, request.provider(), request.amount());
    }

    @PostMapping("/webhooks/payments/{provider}")
    public void handlePaymentWebhook(
            @Parameter(
                    description = "결제 제공자",
                    required = true,
                    in = ParameterIn.PATH,
                    example = "PAYPAL"
            )
            @PathVariable String provider,
            @RequestBody Map<String, Object> webhookData
    ) {
        Long reservationId = (Long) webhookData.get("reservationId");
        String externalPaymentId = (String) webhookData.get("transactionId");
        int amount = (Integer) webhookData.get("amount");

        paymentService.success(provider, reservationId, externalPaymentId, amount);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "결제 상태 조회 성공",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentStatus.class) // ⬅️ Enum 클래스를 직접 참조
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "결제를 찾을 수 없음",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "PAYMENT_NOT_FOUND",
                                                    value = "{\"type\":\"about:blank\",\"title\":\"PAYMENT_NOT_FOUND\",\"status\":404,\"detail\":\"결제를 찾을 수 없습니다.\"}"
                                            )
                                    }
                            )
                    }
            )
    })
    @GetMapping("/payments/{paymentId}/status")
    public PaymentStatus getPaymentStatus(
            @Parameter(
                    description = "결제 ID",
                    required = true,
                    in = ParameterIn.PATH,
                    example = "1"
            )
            @PathVariable Long paymentId
    ) {
        return paymentService.getPaymentStatus(paymentId);
    }
}
