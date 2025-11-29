package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MeetingRoom extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int hourlyRate;

    public MeetingRoom(String name, int capacity, int hourlyRate) {
        this.name = name;
        this.capacity = capacity;
        this.hourlyRate = hourlyRate;
    }

    public int calculateTotalAmount(int hours) {
        return hourlyRate * hours;
    }
}
