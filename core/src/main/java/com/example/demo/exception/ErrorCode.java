package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_DURATION("시작 시간은 종료 시간보다 빨라야 합니다."),
    INVALID_TIME_UNIT("시간은 정시 또는 30분 단위여야 합니다."),
    MEETING_ROOM_ALREADY_RESERVED("회의실이 이미 예약되어 있습니다."),
    PAYMENT_PROVIDER_NOT_SUPPORTED("지원하지 않는 결제 수단입니다."),

    MEETING_ROOM_NOT_FOUND("회의실을 찾을 수 없습니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND("예약을 찾을 수 없습니다."),
    PAYMENT_NOT_FOUND("결제 정보를 찾을 수 없습니다.");

    private final String message;
}
