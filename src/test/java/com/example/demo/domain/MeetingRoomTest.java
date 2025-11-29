package com.example.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MeetingRoomTest {

    @Test
    void calculateTotalAmount() {
        // given
        var meetingRoom = new MeetingRoom("Conference Room A", 10, 50);

        // when
        var totalAmount = meetingRoom.calculateTotalAmount(4);

        // then
        assertThat(totalAmount).isEqualTo(200);
    }
}
