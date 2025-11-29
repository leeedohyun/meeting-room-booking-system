package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
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
}
