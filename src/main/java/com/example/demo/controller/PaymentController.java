package com.example.demo.controller;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.PaymentStatus;
import com.example.demo.service.PaymentService;

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
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

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
    @GetMapping("/{paymentId}/status")
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
