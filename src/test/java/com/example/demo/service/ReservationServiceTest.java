package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
