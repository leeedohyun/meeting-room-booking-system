package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.MeetingRoomResponse;
import com.example.demo.domain.MeetingRoom;
import com.example.demo.service.MeetingRoomService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "회의실", description = "회의실 관련 API")
@RestController
@RequiredArgsConstructor
public class MeetingRoomController {

    private final MeetingRoomService meetingRoomService;

    @ApiResponse(
            responseCode = "200",
            description = "회의실 목록 조회 성공",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MeetingRoomResponse.class)
                    )
            }
    )
    @GetMapping("/meeting-rooms")
    public List<MeetingRoomResponse> getMeetingRooms() {
        List<MeetingRoom> meetingRooms = meetingRoomService.getMeetingRooms();
        return meetingRooms.stream()
                .map(MeetingRoomResponse::of)
                .toList();
    }
}
