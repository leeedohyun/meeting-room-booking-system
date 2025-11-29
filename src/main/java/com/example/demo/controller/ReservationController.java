package com.example.demo.controller;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.ReservationRequest;
import com.example.demo.service.ReservationService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "예약", description = "회의실 예약 관련 API")
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회의실 예약 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_DURATION", value = "{\"type\":\"about:blank\",\"title\":\"INVALID_DURATION\",\"status\":400,\"detail\":\"시작 시간은 종료 시간보다 빨라야 합니다.\"}"),
                                    @ExampleObject(name = "INVALID_TIME_UNIT", value = "{\"type\":\"about:blank\",\"title\":\"INVALID_TIME_UNIT\",\"status\":400,\"detail\":\"시간은 정시 또는 30분 단위여야 합니다.\"}"),
                                    @ExampleObject(name = "MEETING_ROOM_ALREADY_RESERVED", value = "{\"type\":\"about:blank\",\"title\":\"MEETING_ROOM_ALREADY_RESERVED\",\"status\":400,\"detail\":\"이미 예약된 시간입니다.\"}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = {
                                    @ExampleObject(name = "USER_NOT_FOUND", value = "{\"type\":\"about:blank\",\"title\":\"USER_NOT_FOUND\",\"status\":404,\"detail\":\"사용자를 찾을 수 없습니다.\"}"),
                                    @ExampleObject(name = "MEETING_ROOM_NOT_FOUND", value = "{\"type\":\"about:blank\",\"title\":\"MEETING_ROOM_NOT_FOUND\",\"status\":404,\"detail\":\"회의실을 찾을 수 없습니다.\"}")
                            }
                    )
            )
    })
    @PostMapping
    public Long reserve(@RequestBody ReservationRequest request) {
        return reservationService.reserve(
                request.userId(),
                request.meetingRoomId(),
                request.startTime(),
                request.endTime()
        );
    }
}
