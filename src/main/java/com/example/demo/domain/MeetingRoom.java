package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import lombok.Getter;

@Entity
@Getter
public class MeetingRoom extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int hourlyRate;
}
