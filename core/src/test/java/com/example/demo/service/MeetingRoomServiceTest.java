package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.domain.MeetingRoom;

@SpringBootTest
@ActiveProfiles("test")
class MeetingRoomServiceTest {

    @Autowired
    MeetingRoomService meetingRoomService;

    @Test
    void getMeetingRooms() {
        List<MeetingRoom> meetingRooms = meetingRoomService.getMeetingRooms();

        assertThat(meetingRooms).hasSize(2)
                .extracting("id")
                .isNotNull();
    }
}
