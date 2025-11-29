package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.Reservation;
import com.example.demo.domain.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser(User user);

    @Query("""
        SELECT COUNT(r) > 0
        FROM Reservation r
        WHERE r.meetingRoom.id = :meetingRoomId
          AND r.status in ('CONFIRMED', 'PAYMENT_PENDING')
          AND r.startTime < :endTime
          AND r.endTime > :startTime
    """)
    boolean existsOverlappingConfirmedReservation(
            @Param("meetingRoomId") Long meetingRoomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
