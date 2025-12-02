package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class PaymentProvider extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String apiEndpoint;

    @Column(nullable = false)
    private String authInfo;
}
