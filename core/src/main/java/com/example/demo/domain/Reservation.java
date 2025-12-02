package com.example.demo.domain;

import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.springframework.http.HttpStatus;

import com.example.demo.exception.CoreException;
import com.example.demo.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation", indexes = {
        @Index(name = "idx_meeting_room_time", columnList = "meetingRoom_id, startTime, endTime", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetingRoom_id", nullable = false)
    private MeetingRoom meetingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private int totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    public Reservation(
            MeetingRoom meetingRoom,
            User user,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int totalAmount,
            ReservationStatus status
    ) {
        this.meetingRoom = meetingRoom;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public static Reservation reserve(
            MeetingRoom meetingRoom,
            User user,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        validateReservationTime(startTime, endTime);

        long hours = Duration.between(startTime, endTime).toHours();
        int totalAmount = meetingRoom.calculateTotalAmount((int) hours);

        return new Reservation(
                meetingRoom,
                user,
                startTime,
                endTime,
                totalAmount,
                ReservationStatus.PAYMENT_PENDING
        );
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELED;
    }

    private static void validateReservationTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw CoreException.warn(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DURATION);
        }

        if (!(startTime.getMinute() == 0 || startTime.getMinute() == 30) ||
                !(endTime.getMinute() == 0 || endTime.getMinute() == 30)) {
            throw CoreException.warn(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_TIME_UNIT);
        }
    }
}
