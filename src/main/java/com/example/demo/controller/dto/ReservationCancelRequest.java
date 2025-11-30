package com.example.demo.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReservationCancelRequest(
        @Schema(description = "사용자 ID", example = "1")
        Long userId
) {

}
