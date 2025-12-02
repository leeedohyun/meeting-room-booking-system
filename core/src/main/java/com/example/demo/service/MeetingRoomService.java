package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.MeetingRoom;
import com.example.demo.repository.MeetingRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetingRoomService {

    private final MeetingRoomRepository meetingRoomRepository;

    @Transactional(readOnly = true)
    public List<MeetingRoom> getMeetingRooms() {
        return meetingRoomRepository.findAll();
    }
}
