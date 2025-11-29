package com.example.demo.controller.dto;

import com.example.demo.domain.MeetingRoom;

import io.swagger.v3.oas.annotations.media.Schema;

public record MeetingRoomResponse(
        @Schema(description = "회의실 ID", example = "1")
        Long id,

        @Schema(description = "회의실 이름", example = "회의실 A")
        String name,

        @Schema(description = "회의실 수용 인원", example = "10")
        int capacity,

        @Schema(description = "회의실 시간당 요금", example = "5000")
        int hourlyRate
) {

    public static MeetingRoomResponse of(MeetingRoom meetingRoom) {
        return new MeetingRoomResponse(
                meetingRoom.getId(),
                meetingRoom.getName(),
                meetingRoom.getCapacity(),
                meetingRoom.getHourlyRate()
        );
    }
}
