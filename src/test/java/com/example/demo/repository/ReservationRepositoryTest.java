package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.domain.MeetingRoom;
import com.example.demo.domain.Reservation;
import com.example.demo.domain.ReservationStatus;
import com.example.demo.domain.User;

@DataJpaTest
@ActiveProfiles("test")
class ReservationRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MeetingRoomRepository meetingRoomRepository;

    @ParameterizedTest
    @MethodSource("provideOverlappingTestCases")
    void existsOverlappingConfirmedReservation(
            LocalDateTime existingStart,
            LocalDateTime existingEnd,
            LocalDateTime checkStart,
            LocalDateTime checkEnd,
            boolean expected
    ) {
        // given
        var user = userRepository.save(new User("username", "email"));
        var meetingRoom = meetingRoomRepository.save(new MeetingRoom("Conference Room A", 10, 50));
        reservationRepository.save(new Reservation(
                meetingRoom,
                user,
                existingStart,
                existingEnd,
                100,
                ReservationStatus.CONFIRMED
        ));

        entityManager.flush();
        entityManager.clear();

        // when
        var exists = reservationRepository.existsOverlappingConfirmedReservation(
                meetingRoom.getId(),
                checkStart,
                checkEnd
        );

        // then
        assertThat(exists).isEqualTo(expected);
    }

    @Test
    void findByUser() {
        // given
        var user = userRepository.save(new User("username", "email"));
        var meetingRoom = meetingRoomRepository.save(new MeetingRoom("Conference Room A", 10, 50));
        var reservation1 = reservationRepository.save(new Reservation(
                meetingRoom,
                user,
                LocalDateTime.of(2025, 11, 29, 10, 0),
                LocalDateTime.of(2025, 11, 29, 12, 0),
                100,
                ReservationStatus.CONFIRMED
        ));
        var reservation2 = reservationRepository.save(new Reservation(
                meetingRoom,
                user,
                LocalDateTime.of(2025, 11, 30, 10, 0),
                LocalDateTime.of(2025, 11, 30, 12, 0),
                100,
                ReservationStatus.PAYMENT_PENDING
        ));

        // when
        var reservations = reservationRepository.findByUser(user);

        // then
        assertThat(reservations).containsExactlyInAnyOrder(reservation1, reservation2);
    }

    private static Stream<Arguments> provideOverlappingTestCases() {
        return Stream.of(
                // 겹치는 경우 - 시작 시간이 겹침
                Arguments.of(
                        LocalDateTime.of(2025, 11, 29, 10, 0),
                        LocalDateTime.of(2025, 11, 29, 12, 0),
                        LocalDateTime.of(2025, 11, 29, 11, 0),
                        LocalDateTime.of(2025, 11, 29, 13, 0),
                        true
                ),
                // 겹치는 경우 - 완전 포함
                Arguments.of(
                        LocalDateTime.of(2025, 11, 29, 10, 0),
                        LocalDateTime.of(2025, 11, 29, 12, 0),
                        LocalDateTime.of(2025, 11, 29, 10, 30),
                        LocalDateTime.of(2025, 11, 29, 11, 30),
                        true
                ),
                // 겹치지 않는 경우 - 이전 시간대
                Arguments.of(
                        LocalDateTime.of(2025, 11, 29, 10, 0),
                        LocalDateTime.of(2025, 11, 29, 12, 0),
                        LocalDateTime.of(2025, 11, 29, 8, 0),
                        LocalDateTime.of(2025, 11, 29, 10, 0),
                        false
                ),
                // 겹치지 않는 경우 - 이후 시간대
                Arguments.of(
                        LocalDateTime.of(2025, 11, 29, 10, 0),
                        LocalDateTime.of(2025, 11, 29, 12, 0),
                        LocalDateTime.of(2025, 11, 29, 12, 0),
                        LocalDateTime.of(2025, 11, 29, 14, 0),
                        false
                )
        );
    }
}
