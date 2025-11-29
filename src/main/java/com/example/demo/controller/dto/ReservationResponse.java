package com.example.demo.controller.dto;

import java.time.LocalDateTime;

import com.example.demo.domain.Reservation;
import com.example.demo.domain.ReservationStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReservationResponse(
        @Schema(description = "예약 ID", example = "1")
        Long id,

        @Schema(description = "회의실 ID", example = "1")
        Long meetingRoomId,

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "시작 시간", example = "2024-07-01T10:00:00")
        LocalDateTime startTime,

        @Schema(description = "종료 시간", example = "2024-07-01T11:00:00")
        LocalDateTime endTime,

        @Schema(description = "총 금액", example = "5000")
        int totalAmount,

        @Schema(description = "예약 상태", example = "CONFIRMED")
        ReservationStatus status
) {

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getMeetingRoom().getId(),
                reservation.getUser().getId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getTotalAmount(),
                reservation.getStatus()
        );
    }
}
