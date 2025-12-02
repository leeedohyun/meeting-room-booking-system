package com.example.demo.controller.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReservationRequest(
        @Schema(description = "유저 ID", example = "1")
        Long userId,

        @Schema(description = "회의실 ID", example = "1")
        Long meetingRoomId,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(description = "시작 시간", example = "2025-11-29 10:00:00")
        LocalDateTime startTime,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(description = "종료 시간", example = "2025-11-29 11:00:00")
        LocalDateTime endTime
) {

}
