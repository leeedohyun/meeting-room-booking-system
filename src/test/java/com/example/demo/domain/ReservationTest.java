package com.example.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.example.demo.exception.CoreException;
import com.example.demo.exception.ErrorCode;

class ReservationTest {
    
    private User user;
    private MeetingRoom meetingRoom;

    @BeforeEach
    void setUp() {
        user = new User();
        meetingRoom = new MeetingRoom("Conference Room A", 100, 50000);
    }

    @Test
    void reserve() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2025, 12, 1, 9, 30);
        LocalDateTime endTime = LocalDateTime.of(2025, 12, 1, 11, 30);

        // when
        Reservation reservation = Reservation.reserve(meetingRoom, user, startTime, endTime);

        // then
        assertThat(reservation).isNotNull();
        assertThat(reservation.getStartTime()).isEqualTo(startTime);
        assertThat(reservation.getEndTime()).isEqualTo(endTime);
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.PAYMENT_PENDING);
        assertThat(reservation.getTotalAmount()).isEqualTo(100000);
    }

    @Test
    void reserve_Fail_StartTimeIsNotBeforeEndTime() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2025, 12, 1, 10, 0);
        LocalDateTime endTimeEqual = LocalDateTime.of(2025, 12, 1, 10, 0);
        LocalDateTime endTimeBefore = LocalDateTime.of(2025, 12, 1, 9, 30);

        // when & then
        assertThatThrownBy(() -> Reservation.reserve(meetingRoom, user, startTime, endTimeEqual))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.INVALID_DURATION.getMessage())
                .extracting("httpStatus")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThatThrownBy(() -> Reservation.reserve(meetingRoom, user, startTime, endTimeBefore))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.INVALID_DURATION.getMessage());
    }

    @Test
    void reserve_Fail_InvalidTimeUnit() {
        // Case 1: 시작 시간이 15분
        LocalDateTime startTime15 = LocalDateTime.of(2025, 12, 1, 10, 15);
        LocalDateTime endTime = LocalDateTime.of(2025, 12, 1, 11, 0);

        assertThatThrownBy(() -> Reservation.reserve(meetingRoom, user, startTime15, endTime))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.INVALID_TIME_UNIT.getMessage())
                .extracting("httpStatus")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        // Case 2: 종료 시간이 45분
        LocalDateTime startTime = LocalDateTime.of(2025, 12, 1, 10, 30);
        LocalDateTime endTime45 = LocalDateTime.of(2025, 12, 1, 11, 45);

        assertThatThrownBy(() -> Reservation.reserve(meetingRoom, user, startTime, endTime45))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.INVALID_TIME_UNIT.getMessage());
    }
}
