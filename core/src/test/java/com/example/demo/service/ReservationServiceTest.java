package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Reservation;
import com.example.demo.domain.ReservationStatus;
import com.example.demo.exception.CoreException;
import com.example.demo.exception.ErrorCode;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Test
    void reserve() {
        // given
        var userId = 1L;
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 0);
        var endTime = LocalDateTime.of(2025, 11, 29, 18, 30);

        // when
        var reservationId = reservationService.reserve(userId, meetingRoomId, startTime, endTime);

        // then
        assertThat(reservationId).isNotNull();
    }

    @Test
    void reserve_WhenMeetingRoomAlreadyReserved() {
        // given
        var userId = 1L;
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 0);
        var endTime = LocalDateTime.of(2025, 11, 29, 18, 30);

        reservationService.reserve(userId, meetingRoomId, startTime, endTime);

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(userId, meetingRoomId, startTime, endTime))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.MEETING_ROOM_ALREADY_RESERVED.getMessage());
    }

    @Test
    void reserve_WhenUserNotFound() {
        // given
        var userId = 999L;
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 0);
        var endTime = LocalDateTime.of(2025, 11, 29, 18, 30);

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(userId, meetingRoomId, startTime, endTime))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    void reserve_WhenMeetingRoomNotFound() {
        // given
        var userId = 1L;
        var meetingRoomId = 999L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 0);
        var endTime = LocalDateTime.of(2025, 11, 29, 18, 30);

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(userId, meetingRoomId, startTime, endTime))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.MEETING_ROOM_NOT_FOUND.getMessage());
    }

    @Test
    void reserve_WhenInvalidDuration() {
        var userId = 1L;
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 30);
        var endTime = LocalDateTime.of(2025, 11, 29, 18, 0);

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(userId, meetingRoomId, startTime, endTime))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.INVALID_DURATION.getMessage());
    }

    @Test
    void reserve_WhenInvalidStartTime() {
        // given
        var userId = 1L;
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 15);
        var endTime = LocalDateTime.of(2025, 11, 29, 19, 0);

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(userId, meetingRoomId, startTime, endTime))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.INVALID_TIME_UNIT.getMessage());
    }

    @Test
    void reserve_WhenInvalidEndTime() {
        // given
        var userId = 1L;
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 0);
        var endTime = LocalDateTime.of(2025, 11, 29, 19, 5);

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(userId, meetingRoomId, startTime, endTime))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.INVALID_TIME_UNIT.getMessage());
    }

    @Test
    void cancel() {
        // given
        var userId = 1L;
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 0);
        var endTime = LocalDateTime.of(2025, 11, 29, 18, 30);
        var reservationId = reservationService.reserve(userId, meetingRoomId, startTime, endTime);

        // when
        reservationService.cancel(reservationId, userId);

        // then
        List<Reservation> reservations = reservationService.getReservations(userId);
        var reservation = reservations.stream()
                .filter(r -> r.getId().equals(reservationId))
                .findFirst()
                .orElseThrow();
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELED);
    }


    @Test
    void cancel_WhenUserNotFound() {
        // given
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 0);
        var endTime = LocalDateTime.of(2025, 11, 29, 18, 30);
        var reservationId = reservationService.reserve(1L, meetingRoomId, startTime, endTime);
        var invalidUserId = 999L;

        // when & then
        assertThatThrownBy(() -> reservationService.cancel(reservationId, invalidUserId))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    void cancel_WhenReservationNotFound() {
        // given
        var userId = 1L;
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 0);
        var endTime = LocalDateTime.of(2025, 11, 29, 18, 30);
        reservationService.reserve(userId, meetingRoomId, startTime, endTime);
        var invalidReservationId = 999L;

        // when & then
        assertThatThrownBy(() -> reservationService.cancel(invalidReservationId, userId))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.RESERVATION_NOT_FOUND.getMessage());
    }

    @Test
    void getReservations() {
        // given
        var userId = 1L;
        var meetingRoomId = 1L;
        var startTime = LocalDateTime.of(2025, 11, 29, 18, 0);
        var endTime = LocalDateTime.of(2025, 11, 29, 18, 30);

        var reservationId = reservationService.reserve(userId, meetingRoomId, startTime, endTime);

        // when
        var reservations = reservationService.getReservations(userId);

        // then
        assertThat(reservations).hasSize(1)
                .extracting("id")
                .containsExactly(reservationId);
    }

    @Test
    void getReservations_WhenUserNotFound() {
        // given
        var userId = 999L;

        // when & then
        assertThatThrownBy(() -> reservationService.getReservations(userId))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }
}
