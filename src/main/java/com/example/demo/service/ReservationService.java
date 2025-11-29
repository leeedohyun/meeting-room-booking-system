package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.MeetingRoom;
import com.example.demo.domain.Reservation;
import com.example.demo.domain.User;
import com.example.demo.exception.CoreException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.MeetingRoomRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final MeetingRoomRepository meetingRoomRepository;

    @Transactional
    public Long reserve(
            Long userId,
            Long meetingRoomId,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        if (reservationRepository.existsOverlappingConfirmedReservation(meetingRoomId, startTime, endTime)) {
            throw CoreException.warn(HttpStatus.BAD_REQUEST, ErrorCode.MEETING_ROOM_ALREADY_RESERVED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> CoreException.warn(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));
        MeetingRoom meetingRoom = meetingRoomRepository.findById(meetingRoomId)
                .orElseThrow(() -> CoreException.warn(HttpStatus.NOT_FOUND, ErrorCode.MEETING_ROOM_NOT_FOUND));

        Reservation reservation = Reservation.reserve(meetingRoom, user, startTime, endTime);
        return reservationRepository.save(reservation).getId();
    }
}
