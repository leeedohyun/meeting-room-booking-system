package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.MeetingRoom;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {

}
